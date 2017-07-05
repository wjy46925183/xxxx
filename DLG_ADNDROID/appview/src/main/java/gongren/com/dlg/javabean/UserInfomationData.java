package gongren.com.dlg.javabean;

import java.io.Serializable;

/**
 * Created by xinxiaolong on 2017/4/11 0011.
 */

public class UserInfomationData implements Serializable{

    public UserRestVo userRestVo;                       //用户基本信息
    public UserAttributeRestVo userAttributeRestVo;   //用户详细信息

    public class UserRestVo{
        public String id;                             //用户id
        public String username;                      //昵称
        public String phone;                         //手机号
        public String imei;                          //
        public String email;                         //邮箱
        public String weChat;                        //微信账号
        public String oicq;                          //qq号
        public String type;                          //用户类型PERSONAL(个人)  ENTERPRISE(企业)
        public String status;                        //用户状态(0.新建,1.正常,2.冻结)
        public String auditStatus;                  //审核状态(0.未审核,1.审核中,2.审核通过,3.审核失败)
        public String origin;                        //注册来源
        public String messageSendIdentify;         //消息推送标识
    }

    public class UserAttributeRestVo{
        public String userId;                        //用户Id
        public String name;                          //名称
        public String logo;                          //用户头像
        public String releaseCount;                 //发单统计
        public String scoreCount;                   //评分统计
        public String creditCount;                  //诚信统计
        public String distance;                     //距离
        public String description;                  //描述
        public String certificateType;             //证件类型(0.身份证)
        public String certificateNumber;           //证件号码
        public String height;                        //身高
        public String weight;                        //体重
        public String sex;                            //性别(1.男,2.女,3.保密)
        public String identity;                      //用户身份(0.学生,1.自由工作者,2.兼职人员)
        public String personalizedSignature;       //用户个性签名
        public String location;                      //用户地址

        public String getIdentityName(){
            if(identity==null){
                return "自由工作者";
            }
            switch (identity){
                case "0":
                    return "在校学生";
                case "1":
                    return "自由工作者";
                case "2":
                    return "兼职人员";
                default:
                    return "自由工作者";
            }
        }
    }
    public static String getIdentityTypeByName(String identityName){
        switch (identityName){
            case "在校学生":
                return "0";
            case "自由工作者":
                return "1";
            case "兼职人员":
                return "2";
            default:
                return "1";
        }
    }
}
