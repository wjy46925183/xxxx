package com.dlg.data.cache;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.common.cache.ACache;
import com.common.string.StringUtil;

import okhttp.rx.JsonResponse;
import rx.Observable;
import rx.Subscriber;

/**
 * 作者：wangdakuan
 * 主要功能：接口数据缓存
 * 创建时间：2017/6/23 09:48
 */
public class ObjectCacheImpl implements ObjectCache<Object> {
    ACache aCache;

    public ObjectCacheImpl(Context context) {
        aCache = ACache.get(context);
    }

    @Override
    public synchronized Observable<Object> get(final String key, final Class<?> aClass) {
        return Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                String fileContent = aCache.getAsString(key);
                Object userEntity = fileContent;
                if (null != aClass) {
                    userEntity = JSON.parseObject(fileContent, aClass);
                }
                if (userEntity != null) {
                    subscriber.onNext(userEntity);
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(new IllegalStateException());
                }
            }
        });
    }

    @Override
    public synchronized Observable<Object> get(final String key, final Class<?> aClass,final Class<?> bClass) {
        return Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                String fileContent = aCache.getAsString(key);
                Object userEntity = fileContent;
                if (null != aClass) {
                    if(aClass == JsonResponse.class){
                        userEntity = JSON.parseObject(fileContent, aClass);
                        JsonResponse response = (JsonResponse) userEntity;
                        response.setData(JSON.parseObject(response.getData().toString(),bClass));
                        userEntity = response;
                    }
                }
                if (userEntity != null) {
                    subscriber.onNext(userEntity);
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(new IllegalStateException());
                }
            }
        });
    }


    @Override
    public synchronized Observable<Object> getList(final String key, final Class<?> aClass,final Class<?> bClass) {
        return Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                String fileContent = aCache.getAsString(key);
                Object userEntity = fileContent;
                if (null != aClass) {
                    if(aClass == JsonResponse.class){
                        userEntity = JSON.parseObject(fileContent, aClass);
                        JsonResponse response = (JsonResponse) userEntity;
                        response.setData(JSON.parseArray(response.getData().toString(),bClass));
                        userEntity = response;
                    }
                }
                if (userEntity != null) {
                    subscriber.onNext(userEntity);
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(new IllegalStateException());
                }
            }
        });
    }


    @Override
    public synchronized Observable<Object> getList(final String key, final Class<?> aClass) {
        return Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {

                String fileContent = aCache.getAsString(key);
                Object userEntity = fileContent;
                if (null != aClass) {
                    userEntity = JSON.parseArray(fileContent, aClass);
                }
                if (userEntity != null) {
                    subscriber.onNext(userEntity);
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(new IllegalStateException());
                }
            }
        });
    }

    @Override
    public void put(Object objectEntity, String key) {
        aCache.put(key, JSON.toJSONString(objectEntity), ACache.TIME_DAY);
    }

    public void put(String key, String object) {
        aCache.put(key, object);
    }

    public String getAsString(String key) {
        return aCache.getAsString(key);
    }

    @Override
    public boolean isCached(String key) {
        return StringUtil.isEmpty(aCache.getAsString(key)) ? false : true;
    }
}

