package gongren.com.dlg.utils;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * 友盟分享工具类
 * shareUmeng
 **/
public class ShareUtils {

    private static UMShareListener mShareListener;

    private static ShareAction mShareAction;

    /**
     * 初始化分享
     */
    public static void initShare() {

        mShareListener = new UMShareListener() {
            @Override
            public void onResult(SHARE_MEDIA share_media) {
                ToastUtils.showToastShort(context, "分享成功");
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                ToastUtils.showToastShort(context, "分享失败");
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
                ToastUtils.showToastShort(context, "取消分享");
            }
        };
    }

    public static ShareAction setUMShareAction(Context context, String title, String content, String url,String logoUrl) {
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

    public static ShareAction setUMShare(Context context,String type, String title, String content, String url,String logoUrl){
        SHARE_MEDIA shareMedia = null;
        if(type.toLowerCase().contains("dlg://inviteqq")){
            shareMedia = SHARE_MEDIA.QQ;
        }
        if(type.toLowerCase().contains("dlg://inviteweixin")){
            shareMedia = SHARE_MEDIA.WEIXIN;
        }
        mShareAction =  new ShareAction((Activity) context).setPlatform(shareMedia).setCallback(mShareListener)
                .withTitle(title)
                .withText(content)
                .withTargetUrl(url);
        if(!TextUtils.isEmpty(logoUrl)){
            UMImage umImage = new UMImage(context,logoUrl);
            mShareAction.withMedia(umImage);
        }
        return mShareAction;
    }

    public static ShareAction setUMShareInvitation(Context context, String title, String content, String url,String logoUrl) {
        mShareAction = new ShareAction((Activity) context).setDisplayList(
                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.QQ)
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



//    // 友盟分享平台的Controller,负责管理整个SDK的配置、操作等处理
//    public final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
//
//    /**
//     * 友盟分享
//     * shareUmeng
//     *
//     * @param context
//     * @param msgTitle
//     * @param msgText
//     * @param imgPath  void
//     */
//    public static void shareUmeng(Context context, String msgTitle, String msgContent, String msgSinaContent, String imgPath, String msgUrl) {
//        // 配置需要分享的相关平台
//        configPlatforms(context);
//        // 设置分享的内容
//        setShareContent(context, msgTitle, msgContent, msgSinaContent, imgPath, msgUrl);
//
//    }
//
//    /**
//     * 根据不同的平台设置不同的分享内容</br>
//     *
//     * @param context
//     * @param imgPath
//     * @param msgText
//     * @param msgTitle
//     * @param msgUrl
//     */
//    private static void setShareContent(Context context, String msgTitle, String msgContent, String msgSinaContent, String imgPath, String msgUrl) {
////        LogUtil.i("Share=" + msgContent + imgPath + msgUrl);
//        // 配置SSO
//        mController.getConfig().setSsoHandler(new SinaSsoHandler());
//        //微信
//        WeiXinShareContent weixinContent = new WeiXinShareContent();
//        // 设置朋友圈分享的内容
//        CircleShareContent circleMedia = new CircleShareContent();
//        //新浪
//        SinaShareContent sinaContent = new SinaShareContent();
//        //本地图片
////      UMImage localImage = new UMImage(context, R.drawable.ic_about_app);
//        //url
//        UMImage urlImage = new UMImage(context, imgPath);
//        // 设置分享图片，参数2为本地图片的路径(绝对路径)
////      UMImage urlImage = new UMImage(context, BitmapFactory.decodeFile(imgPath));
//        weixinContent.setShareImage(urlImage);
//        circleMedia.setShareImage(urlImage);
//        sinaContent.setShareImage(urlImage);
//        //添加微信分享内容
//        weixinContent.setShareContent(msgContent);//分享内容摘要
//        weixinContent.setTitle(msgTitle);          //标题
//        weixinContent.setTargetUrl(msgUrl);       //url
//        mController.setShareMedia(weixinContent);
//        //添加朋友圈分享内容
//        circleMedia.setShareContent(msgContent);
//        circleMedia.setTitle(msgTitle);
//        circleMedia.setTargetUrl(msgUrl);
//        mController.setShareMedia(circleMedia);
//        //添加新浪分析内容
//        sinaContent.setShareContent(msgSinaContent);
//        sinaContent.setTitle(msgTitle);
//        sinaContent.setTargetUrl(msgUrl);
//        mController.setShareMedia(sinaContent);
//    }
//
//    /**
//     * 配置分享平台参数</br>
//     *
//     * @param context
//     */
//    private static void configPlatforms(Context context) {
//        // 添加新浪SSO授权
//        mController.getConfig().setSsoHandler(new SinaSsoHandler());
//        // 添加微信、微信朋友圈平台
//        addWXPlatform(context);
//// 打开分享选择页并设置分享平台顺序
//        mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA);
//    }
//
//    /**
//     * @param context
//     * @return
//     * @功能描述 : 添加微信平台分享
//     */
//    private static void addWXPlatform(Context context) {
//        // 注意：在微信授权的时候，必须传递appSecret
//        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
//        String appId = "xxxxxxxxxxxxxxx";//这个填写通过的审核的APPID
//        String appSecret = "xxxxxxxxxxxxxxxxxxxxxxx";
//        // 添加微信平台
//        UMWXHandler wxHandler = new UMWXHandler(context, appId, appSecret);
//        wxHandler.addToSocialSDK();
//        // 支持微信朋友圈
//        UMWXHandler wxCircleHandler = new UMWXHandler(context, appId, appSecret);
//        wxCircleHandler.setToCircle(true);
//        wxCircleHandler.addToSocialSDK();
//    }
}
