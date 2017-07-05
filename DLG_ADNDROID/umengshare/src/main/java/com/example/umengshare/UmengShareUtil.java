package com.example.umengshare;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * 友盟分享工具类
 * shareUmeng
 **/
public class UmengShareUtil {

    private static UMShareListener mShareListener;

    private static ShareAction mShareAction;

    /**
     * 初始化分享
     */
    public static void initShare() {

        mShareListener = new UMShareListener() {
            @Override
            public void onResult(SHARE_MEDIA share_media) {
                Toast.makeText(context,"分享成功",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                Toast.makeText(context,"分享失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
                Toast.makeText(context,"分享取消",Toast.LENGTH_SHORT).show();
            }
        };
    }

    public static ShareAction setUMShareAction(Context context, String title, String content, String url, String logoUrl) {
        mShareAction = new ShareAction((Activity) context).setDisplayList(
                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ,SHARE_MEDIA.QZONE)
                .withTitle(title)
                .withText(content)
                .withTargetUrl(url)
                .setCallback(mShareListener);
        if(null !=logoUrl){
            UMImage umImage = new UMImage(context,logoUrl);
            mShareAction.withMedia(umImage);
        }
        return mShareAction;

    }

    public static void openShare() {
        mShareAction.open();
    }
}
