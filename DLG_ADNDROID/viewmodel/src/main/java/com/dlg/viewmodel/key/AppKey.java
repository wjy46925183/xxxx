package com.dlg.viewmodel.key;

/**
 * 作者：wangdakuan
 * 主要功能：整个项目中的key值
 * 创建时间：2017/6/22 14:24
 */
public class AppKey {
    /**
     * 缓存的key值
     */
    public static class CacheKey{
        public static final String NO_FIRST_ENTER = "noFirstEnter"; //是否第一次进入app
        public static final String MAP_LOCATION_LATLNG = "map_location_latlng"; //当前位置经纬度
        public static final String MY_USER_ID = "my_user_id"; //当前登录者的ID
        public static final String MY_USER_INFO = "my_user_info"; //当前登录者的基本信息
    }
}
