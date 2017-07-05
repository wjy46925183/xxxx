package com.dlg.data.user.factory;

import android.content.Context;

import com.common.sys.SystemUtil;
import com.dlg.data.cache.ObjectCache;
import com.dlg.data.cache.ObjectCacheImpl;
import com.dlg.data.user.UserDiaskSource;
import com.dlg.data.user.UserSource;
import com.dlg.data.user.interactor.UserInteractor;

/**
 * 作者：wangdakuan
 * 主要功能：用户接口工厂
 * 创建时间：2017/6/23 19:14
 */
public class UserFactory {
    private ObjectCache objectCache;
    private Context context;

    public UserFactory(Context applicationContext) {
        this(applicationContext, new ObjectCacheImpl(applicationContext));
    }

    public UserFactory(Context context, ObjectCacheImpl objectCache) {
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
    public UserInteractor createUserData(String name) {
        return createUserData(name, false);
    }

    /**
     * 数据经常更新时每次都会优先取网络
     *
     * @param name   缓存的key
     * @param isWifi 是否判断网络  如果true 判断网络，当前手机没网情况下进行读取本地缓存数据
     * @return
     */
    public UserInteractor createUserData(String name, boolean isWifi) {
        UserInteractor dataInteractor = null;
        if (isWifi) {
            if (!SystemUtil.isNetworkAvailable(context)) {
                if (this.objectCache.isCached(name)) {
                    dataInteractor = new UserDiaskSource(this.objectCache);
                }
            }
        } else {
            if (this.objectCache.isCached(name)) {
                dataInteractor = new UserDiaskSource(this.objectCache);
            }
        }
        if(null == dataInteractor){
            dataInteractor = createUserStore();
        }
        return dataInteractor;
    }

    /**
     * 开启网络请求
     */
    private UserInteractor createUserStore() {
        return new UserSource(this.objectCache);
    }
}
