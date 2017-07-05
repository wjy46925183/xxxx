package com.dlg.data.home.factory;

import android.content.Context;

import com.common.sys.SystemUtil;
import com.dlg.data.cache.ObjectCache;
import com.dlg.data.cache.ObjectCacheImpl;
import com.dlg.data.home.HomeDiaskSource;
import com.dlg.data.home.HomeSource;
import com.dlg.data.home.interactor.HomeInteractor;

/**
 * 作者：wangdakuan
 * 主要功能：首页模块工厂接口实现类
 * 创建时间：2017/6/23 09:59
 */
public class HomeFactory {
    private ObjectCache objectCache;
    private Context context;

    public HomeFactory(Context applicationContext) {
        this(applicationContext, new ObjectCacheImpl(applicationContext));
    }

    public HomeFactory(Context context, ObjectCacheImpl objectCache) {
        if (null == context || objectCache == null) {
            throw new IllegalArgumentException("Constructor parameters cannot be null!!!");
        }
        this.context = context;
        this.objectCache = objectCache;
    }

    /**
     * 数据不经常更新时每次都会优先取缓存
     *
     * @param name 缓存时的KEY
     * @return
     */
    public HomeInteractor createHomeData(String name) {
        return createHomeData(name, false);
    }

    /**
     * 数据经常更新时每次都会优先取网络
     *
     * @param name   缓存的key
     * @param isWifi 是否判断网络  如果true 判断网络，当前手机没网情况下进行读取本地缓存数据
     * @return
     */
    public HomeInteractor createHomeData(String name, boolean isWifi) {
        HomeInteractor dataInteractor = null;
        if (isWifi) {
            if (!SystemUtil.isNetworkAvailable(context)) {
                if (this.objectCache.isCached(name)) {
                    dataInteractor = new HomeDiaskSource(this.objectCache);
                }
            }
        } else {
            if (this.objectCache.isCached(name)) {
                dataInteractor = new HomeDiaskSource(this.objectCache);
            }
        }
        if(null == dataInteractor){
            dataInteractor = createHomeStore();
        }
        return dataInteractor;
    }

    /**
     * 开启网络请求
     */
    private HomeInteractor createHomeStore() {
        return new HomeSource(this.objectCache);
    }
}

