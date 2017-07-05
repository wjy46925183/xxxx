package com.dlg.data.cache;

import rx.Observable;

/**
 * 作者：wangdakuan
 * 主要功能：缓存接口
 * 创建时间：2017/6/23 09:49
 */
public interface ObjectCache<T> {
    /**
     * 获取保存的数据
     * @param key 缓存数据的key
     * @param aClass 缓存数据的对象
     * @return
     */
    Observable<T> get(final String key, Class<?> aClass);

    /**
     * 获取保存的数据
     * @param key 缓存数据的key
     * @param aClass 缓存数据的JsonResponse对象
     * @param bClass 缓存数据的JsonResponse中的泛型对象
     * @return
     */
    Observable<T> get(final String key, Class<?> aClass, Class<?> bClass);

    /**
     * 获取保存的List数据
     * @param key 缓存数据的key
     * @param aClass 缓存数据的对象
     * @return
     */
    Observable<T> getList(final String key, Class<?> aClass);

    /**
     * 获取保存的List数据
     * @param key 缓存数据的key
     * @param aClass 缓存数据的JsonResponse对象
     * @param bClass 缓存数据的JsonResponse中的泛型对象
     * @return
     */
    Observable<T> getList(final String key, Class<?> aClass, Class<?> bClass);

    /**
     * 数据加入缓存
     */
    void put(T objectEntity, final String key);

    /**
     * 缓存是否存在
     */
    boolean isCached(final String key);
}
