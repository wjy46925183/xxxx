package com.dlg.data.common.factory;

import android.content.Context;

import com.common.sys.SystemUtil;
import com.dlg.data.cache.ObjectCache;
import com.dlg.data.cache.ObjectCacheImpl;
import com.dlg.data.common.CommonDiaskSource;
import com.dlg.data.common.CommonSource;
import com.dlg.data.common.interactor.CommonInteractor;

/**
 * 作者：wangdakuan
 * 主要功能：接口创建工厂类
 * 创建时间：2017/6/23 14:54
 */
public class CommonFactory {
    private ObjectCache objectCache;
    private Context context;

    public CommonFactory(Context applicationContext) {
        this(applicationContext, new ObjectCacheImpl(applicationContext));
    }

    public CommonFactory(Context context, ObjectCacheImpl objectCache) {
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
    public CommonInteractor createData(String name) {
        return createData(name, false);
    }

    /**
     * 数据经常更新时每次都会优先取网络
     *
     * @param name   缓存的key
     * @param isWifi 是否判断网络  如果true 判断网络，当前手机没网情况下进行读取本地缓存数据
     * @return
     */
    public CommonInteractor createData(String name, boolean isWifi) {
        CommonInteractor dataInteractor = null;
        if (isWifi) {
            if (!SystemUtil.isNetworkAvailable(context)) {
                if (this.objectCache.isCached(name)) {
                    dataInteractor = new CommonDiaskSource(this.objectCache);
                }
            }
        } else {
            if (this.objectCache.isCached(name)) {
                dataInteractor = new CommonDiaskSource(this.objectCache);
            }
        }
        if(null == dataInteractor){
            dataInteractor = createStore();
        }
        return dataInteractor;
    }

    /**
     * 开启网络请求
     */
    private CommonInteractor createStore() {
        return new CommonSource(this.objectCache);
    }
}
