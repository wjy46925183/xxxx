package gongren.com.dlg.alipayUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import gongren.com.dlg.alipayUtils2_0.SignUtils;


/**
 * 支付宝支付工具类
 */
public class AlipayUtils {
    private Context context;
    private String orderID;       //订单号
    private Handler mHandler;
    private static final int SDK_PAY_FLAG = 1;
    private String alipayNotify;

    public AlipayUtils(Context context, String orderID, String alipayNotify, Handler mHandler) {
        this.context = context;
        this.orderID = orderID;
        this.alipayNotify = alipayNotify;
        this.mHandler = mHandler;
    }
    //客户端使用的私钥，对于请求进行数字签名，传给服务器，服务器使用公钥进行验证
//    public static final String RSA_PRIVATE = "";
//    public static final String RSA2_PRIVATE = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC8tFpv15GWDERwnW5/8yVBUdJe5rmepKSf4GZ6XRiC+9Hnzw+E5Y9dBcTIqFmlqVMbY9S2xOBS0TL6Rnb6KnNymGKQD38Ft/o8z1N7dfFqu4JROq9Z1nRSAy7bABvZsW/7mQPdc1hgRi8uWInpn+cxHV0Xu6o88K8HMbiEehsJ/oAdtimlkHZ27Zwpdrb+nQtsMBRBEi3m4mQNmCC8Rw8ofKk6iQLw7Y44Jw3jb3FcbjG7XiJW4Txw4VJ4rNR4ajMb8Ul5jcZ72d1yeaUHNoa197KsJetFELy10GRRm+DH2KKnikCe7S4GnbcL2pGpAxGpZG9tpygeRkaa3ihIvq8RAgMBAAECggEBAKXL3E253Ds2agd7+vNGQWxjpG8nTt1ZER87PS6zju6+rA18usjsHQsBH4WhjKpl3BQLoYA3+FQrZBvLP4vLMoXz511lkBgJLj6xdkraNJOvTe9qnVhUePz5uPhH3zBkmtw289NlHbfw0lFpDzsJ+EoJ35oTk55ZZZ7vi4wx8IHU7Kc3nsl1toFolYpm81yJ+kkQxkOAPHthWVlV7JJaVGENVT5b1FsktXJSxcwFokSg8n1Rei0Yom1w6NjR3LubEmuGbAPweWYqkfh9/pE9IIM8BMz7xLWXxREMhSKmVaT3m6zGMhMLa10QcbCC/VnOYZGoF+q7ytG8RIP3ktAqqEECgYEA3LYx5M+R6Wy60c9Ep2tTVuY2Bcr4D4UoEjsPCUdGLtdR66bM3yCf0ynxmgEJshzEFcSGJRnSoYXVdnHQInTHegCBgqtCIoMlkqV39llyrBPp2JsThTNWl+fhHzQY0EXadRbf7a/2tpjYisB78O6nilAbvgqr7QEeDSb/dnaxlnkCgYEA2uAXz5VjekubMszYMlqA04v18UuA512Erk/lSXIVOM+sFGTNth6uKO1aQD3X7huAID+4omtZRZrwOzcHxzpev9p5QwJ4cwhSqMzorsaJ/doXqxGm2VrEUUldJly2hAUlaQlOQjql0yQurhaqL8xcN1Xlze4fNGuizlPNGB9Al1kCgYBFkiC0EUHbJGzXCdCIPWptr+9DcDE1IzH4XSlmMVNMHI80CsX1Z2E5vLTtaHpX7H1apzHpq/Qa+gbJaSLEWa8vI82vFLCqFFDZotLnklZ1K2nA31EN4ZrgEzWBEnjn3bz4v5ciYx8Pe6p2QipPTcr97DZydwaulHWWi75m5w8fcQKBgEvnu1o6LK2U2tXbRH7x9AtpVnm//Yw/Wmw5OQMnlUSGgN7xtoWaTjvTVKFC0Ue6Mcz9TELHh8Yj3XwrsX7eN03BrpwWlz8Ne90ecvNp1yE6KUsibAKk/8BQ+QT80i8mubqbh5BgVnHK8oVdRtgcvz5Rg8jO8/NyAJwdY9shu3zBAoGAVsVFHTsZzUZvYyntp8RO2wznncllhUyoKSUzRAhfQmad824ymqh0aEWd0yvAbZdul2l4o2e6/9n5AIBzqi2Cd5HHMi1Oq4bvOrUjKG8oISL8Ja4StQ7Ltsbb9Vw9rJ10Fg4X8g7I7dfhtl9WiZbp+Umk7wyvGZ0bcLAba44rmuM=";
//    private String APPID = "2016101702209522";


    // 商户合作ID 必须以 2088 开头
    public static final String PARTNER = "2088221310008922";

    // 收款人，支付宝账号 账号是固定的，一个商户一个账号
    public static final String SELLER = "zfb@itrusty.com";

    //客户端使用的私钥，对于请求进行数字签名，传给服务器，服务器使用公钥进行验证
    public static final String RSA_PRIVATE =
            "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANaWFOOB739dEcFD" +
                    "Q90yReKgmNs4IXtfQkM2Fgr0BFBZTkjChzN5SMG36TGyKQi6/zM6DTqMRQVxeP2E" +
                    "kRokCPfpf2+r3N8EkzwhlUAH7J6DHGxk998q0o2cv5BHxOl1JTX/A1iP+Fm3mWpf" +
                    "CpMBZMBBAI8V+PfRBoM70tCVWIupAgMBAAECgYAGAUkwXLCBFe+rQuChTgjaoLfm" +
                    "WpnFRBMsolTXlaCRRw6Dxr3V2O0KTsU92MiBMAp4OoWoP8imsFg8b/tHif6eNFJF" +
                    "bI8UUhM9+1i1d7Y//rdLc0n9bSitVMwis53rpVd3oMthqJ9Imw1DjvdzzitK4ghy" +
                    "fG+H1q3cHSzLU7YzyQJBAOwWH/W2A5wuuigxnmJ9C3n3lxqiJWmiUc61y1gBU2ck" +
                    "aCno0/+oP6UDzHA2HlAyb2vNZqJ2u9yfmjQJh8oYuRcCQQDor7KzkagohgAT0GUB" +
                    "4YRiTHPHM9iVEJ5s9gSfRp7P3ysKAVREPy4gSDOJWYNDDMKMtXVhFMLD3kjrvgwk" +
                    "plk/AkEAssQaFTtqxmPXkEHqNZeMDiH9qCwpejBwE78yp4PxfINj4IAtr0PeVXxn" +
                    "3HwOPhnCpvWUhuazX972q/qfeNd5pwJBAMofvWqw71LsO+r6TpOYs6ez46q5xbyP" +
                    "KB9cgd1duhy2LivNxcZW35mZPnNkN1qqHeGeyuze3OTKFav6dY/slH8CQDLw8p0v" +
                    "1obPQ/hjE8a+nd6U6HC3o6ZCRwDKSjLa8Kv1Jr006ehw7VDKAWEoobrFTASlL8Pw" +
                    "HzyeZvftyr+ETq8=";

    /**
     * 调用支付宝SDK支付
     *
     * @param product     商品名称
     * @param productinfo 商品信息
     * @param price       价格
     */
    public void aliPay(String product, String productinfo, String price) {

//        /**
//         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
//         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
//         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
//         *
//         * orderInfo的获取必须来自服务端；
//         */
//        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
//        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2, orderID, product, productinfo, price);
//        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
//
//        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
//        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
//        final String orderInfo = orderParam + "&" + sign;
//
//        Runnable payRunnable = new Runnable() {
//
//            @Override
//            public void run() {
//                PayTask alipay = new PayTask((Activity) context);
//                Map<String, String> result = alipay.payV2(orderInfo, true);
//                Log.i("msp", result.toString());
//
//                Message msg = new Message();
//                msg.what = SDK_PAY_FLAG;
//                msg.obj = result;
//                mHandler.sendMessage(msg);
//            }
//        };
//
//        Thread payThread = new Thread(payRunnable);
//        payThread.start();








        if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE)
                || TextUtils.isEmpty(SELLER)) {
            new AlertDialog.Builder(context)
                    .setTitle("警告")
                    .setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialoginterface, int i) {
                                    ((Activity) context).finish();
                                }
                            }).show();
            return;
        }
        // 订单
        String orderInfo = getOrderInfo(product, productinfo, price);

        /**
         * 对订单做RSA 签名
         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
         */
        String sign = sign(orderInfo);
        try {
            /**
             * 仅需对sign 做URL编码
             */
            if (sign != null && sign.length() > 0) {
                sign = URLEncoder.encode(sign, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask((Activity) context);
                // 调用支付接口，获取支付结果
                Map<String, String> result = alipay.payV2(payInfo, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 创建订单信息
     */
    public String getOrderInfo(String subject, String body, String price) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + orderID + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
//        orderInfo += "&notify_url=" + "\"" + GetDataConfing.alipayNotify + "\"";
        orderInfo += "&notify_url=" + "\"" + alipayNotify + "\"";
        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    public String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE,false);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    public String getSignType() {
        return "sign_type=\"RSA\"";
//        return "sign_type=\"MD5\"";
    }

}
