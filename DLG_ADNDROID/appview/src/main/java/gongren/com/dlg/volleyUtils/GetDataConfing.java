package gongren.com.dlg.volleyUtils;

/**
 * 项目接口
 */
public class GetDataConfing {

    /**
     * 测试环境:http://10.20.33.33:8701
     * 正式环境：dlg.dalinggong.com
     * 预发布环境：http://dlgprev.gongren.com
     */

//    public static final String BASEURL = "http://dlg.dalinggong.com/v_2_4";
//    public static final String BASEURL = "http://dlgprev.dalinggong.com";
    public static final String BASEURL = "http://10.20.31.201:8080";
//    public static final String BASEURL = "http://10.20.31.45:8123/gongren-odd-job-restful-api";

    //public static final String BASEURL = "http://10.20.36.78:8080/gongren-odd-job-restful-api";
//    public static final String BASEURL = "http://10.20.31.43:8080/gongren-odd-job-restful-api";
//     public static final String BASEURL = "http://10.20.31.45:8123/gongren-odd-job-restful-api";

    /**
     * 短信验证码接口
     * 测试环境：http://apitest.chengxinhutong.com
     * 正式环境：http://api.itrusty.com
     * 预发布环境：http://apiprev.chengxinhutong.com
     */
    public static final String CODEURL = BASEURL + "/api/payBindRest/sendBindSms";
//    public static final String CODEURL = "http://apitest.chengxinhutong.com/verifycode/getmobilevc?bizsys=dlg&smstype=4&mobile=";
    //    public static final String CODEURL = "http://api.itrusty.com/verifycode/getmobilevc?bizsys=dlg&smstype=4&mobile=";
    //    public static final String CODEURL = "http://apiprev.chengxinhutong.com/verifycode/getmobilevc?bizsys=dlg&smstype=4&mobile=";
//    public static final String CODEURL = "http://api.itrusty.com/verifycode/getmobilevc?bizsys=dlg&smstype=4&mobile=";
//    public static final String CODEURL = "http://apiprev.chengxinhutong.com/verifycode/getmobilevc?bizsys=dlg&smstype=4&mobile=";

    /**
     * 发送快捷支付短信
     */
    public static final String sendQuickPayMessage = BASEURL + "/api/userAccountRest/sendQuickPayMessage";
    /**
     * 支付宝服务器异步通知页面路径
     * 测试：http://paytest.chengxinhutong.com/notify/alipayNotify
     * 正式：http://pay.itrusty.com/notify/alipayNotify
     * 预发布：http://payprev.chengxinhutong.com/notify/alipayNotify
     */
    public static final String alipayNotify = "http://paytest.chengxinhutong.com/notify/alipayNotify";
    //    public static final String alipayNotify = "http://pay.itrusty.com/notify/alipayNotify";
    //        public static final String alipayNotify = "http://pay.itrusty.com/notify/alipayNotify";
    //    public static final String alipayNotify = "http://pay.itrusty.com/notify/alipayNotify";
//        public static final String alipayNotify = "http://pay.itrusty.com/notify/alipayNotify";

    /******
     * 用户协议(测试地址)
     * liukui 2017/04/18
     */
    public static final String SYSTEM_USERAGREE = "http://baike.baidu.com/link?url=wLGL3ecPMWWxSKERtyOmSfwnnRA9uUQZBO8E6BOsx4d1WF58iJCW7OTe_yShpXJukzftjMZfrkkCO_R9mxKK9fRntl3mi43nQmGG_uXXCMgxNIUEDXLC4BW3Kjx66H6lkSdrJxSe0fUVYiX-BF47T_";

    /**
     * 根据用户手机号来判断用户是否注册
     * liukui 2017/04/18
     */
    public static final String SYSTEM_CHECKMOBILE = BASEURL + "/verifyPhone";

    /**
     * 登录
     * liukui 2017/04/18
     */
    public static final String LOGIN = BASEURL + "/login";
    //    public static final String LOGIN = BASEURL + "/oapi/user/login";
    public static final String SYSTEM_LOGIN = BASEURL + "/login";

    /**
     * 注册
     * liukui 2017/04/18
     */
    public static final String SYSTEM_REGISTER = BASEURL + "/register";
    /**
     * 注册---检查验证码
     * liukui 2017/04/18
     */
    public static final String REGISTER_CHECKVERIFYCODE = BASEURL + "/api/smsRest/checkVerifyCode";

    /**
     * 忘记密码
     * liukui 2017/04/18
     */
    public static final String SYSTEM_FORGETPWD = BASEURL + "/forget";
    /*****
     * 修改密码
     */
    public static final String SYSTEM_MODIFYPWD = BASEURL + "/api/userRest/updateUserPassword";

    /**
     * 发送短信 (获取短信验证码)
     * liukui 2017/04/18
     */
    public static final String SYSTEM_NOTECODE = BASEURL + "/api/smsRest/sendVerifyCode";

    /*****
     * 获取语音验证码
     * liukui 2017/04/18
     */
    public static final String SYSTEM_VOICECODE = BASEURL + "/api/smsRest/sendVoiceMessage";
    /*****
     * 新增登录信息
     * liukui 2017/04/18
     */
    public static final String SYSTEM_ADDUSERMESG = BASEURL + "/api/userLoginRestApi/addUserLogin";

    /*****
     * (RPC)雇主查询是否有进行中的订单
     * liukui 2017/04/11
     */
    public static final String BOSS_IS_TASKING = BASEURL + "/api/jobTaskRest/isOrderByEmployerId";
    /*****
     * 1.1.6（REST）首页-雇主-有人接单、等待验收-列表
     */
    public static final String BOSS_IS_TASKING_LIST = BASEURL + "/api/jobTaskRest/getJobTaskOfOrderList";
    /*****
     * 4.2.2(REST)查询订单操作配置
     */
    public static final String BOSS_ORDERFLOW_CONFIG = BASEURL + "/api/orderFlowConfigRest/findByBusinessNumber";
    /*****
     * 4.4.2 (REST)更新订单操作状态
     */
    public static final String BOSS_UPDATA_ORDER_OPERASTATUS = BASEURL + "/api/orderRest/updateOrderOperaStatus";
    /*****
     * (RPC)雇员查询是否有进行中的订单
     * liukui 2017/04/11
     */
    public static final String WORKER_IS_TASKING = BASEURL + "/api/jobTaskRest/isOrderByEmployeeId";
    /*****
     * 获取数据字典列表(工种)
     * liukui 2017/04/11
     */
    public static final String COMMON_HOT_CRAFT = BASEURL + "/api/dictionaryRest/queryGroup";

    /*****
     * 首页-地图数据(雇员)
     * liukui 2017/04/11
     */
    public static final String WORK_MAP_LIST = BASEURL + "/api/jobTaskRest/getJobTaskMapList";

    /*****
     * （RPC）查询任务详情(雇员)
     * liukui 2017/04/12
     */
    public static final String WORK_MAP_DETAIL = BASEURL + "/api/jobTaskRest/getJobTaskById";

    /*****
     * 首页-地图数据(雇主)
     * liukui 2017/04/11
     */
    public static final String BOSS_MAP_LIST = BASEURL + "/api/jobTaskRest/getJobTaskMapListByEmployeeId";

    /*****
     * 雇员详细信息(雇主)
     * liukui 2017/04/11
     */
    public static final String BOSS_MAP_DETAIL = BASEURL + "/api/userRest/queryUserDetail";

    /**
     * 个人详情页---记录列表
     */
    public static final String queryReleaseJoinLateCancel = BASEURL + "/api/userRest/queryReleaseJoinLateCancel";

    /**
     * app检查更新接口
     */
    public static final String SYSTEM_DETRCTVERSION = BASEURL + "/api/sysGencode/queryAppVersion";

    /**
     * 退出登录
     */
    public static final String LOGOUT = BASEURL + "/logout";

    /**
     * 完成订单列表
     */
    public static final String queryMissionList = BASEURL + "/api/mission/queryMissionList";

    /**
     * 零工类型
     */
    public static final String querySysPosition = BASEURL + "/api/sysGencode/querySysPosition";

    /**
     * 个人中心
     */
    public static final String MYINFO = BASEURL + "/api/user/queryStatistics";

    /**
     * 保存登录坐标
     */
    public static final String SAVE_LATLNG = BASEURL + "/api/user/adduserLogin";

    /**
     * 发布实时/预约订单
     */
    public static final String addMission = BASEURL + "/api/mission/addMission";

    /**
     * 修改密码
     */
    public static final String CHANGE_PWD = BASEURL + "/api/user/updatePassword";

    /**
     * 查询消息列表
     */
    public static final String QueryMessageList = BASEURL + "/api/message/queryMessageList";

    /**
     * (雇员端)雇员接单订单详情
     */
    public static final String queryDetail = BASEURL + "/api/mission/queryMisDetail";

    /**
     * (雇主端)雇员接单订单详情
     */
    public static final String BoQueryDetail = BASEURL + "/api/mission/queryDetail";

    /**
     * 查询用户详情
     */
    public static final String queryUserInfo = BASEURL + "/api/user/queryUserInfo";

    /**
     * 获取雇员的订单列表
     */
    public static final String ordOrderList = BASEURL + "/api/orderRest/findPageListByUserVoAndStatus";

    /**
     * 获取订单违约金及扣除的诚信值
     */
    public static final String cancleOrderDetail = BASEURL + "/api/orderCancelRest/orderCancelFare";

    /**
     * 获取雇员取消原因
     */
    public static final String CancleReasons = BASEURL + "/api/dictionaryRest/queryGroup";

    /**
     * 取消订单
     */
    public static final String CancleOrder = BASEURL + "/api/orderCancelRest/cancel";

    /**
     * 获取雇员的订单列表某个订单的倒计时
     */
    public static final String OrderRemainTime = BASEURL + "/api/orderRest/findRemainingTime";

    /**
     * 订单评价
     */
    public static final String OrderPingJia = BASEURL + "/api/orderEvaluateRest/addOrderEvaluate";

    /**
     * 获取订单评价标签
     */
    public static final String OrderPingJiaTabs = BASEURL + "/api/dictionaryRest/queryGroup";

    /**
     * 提交评价
     */
    public static final String CommitPJ = BASEURL + "/api/orderEvaluateRest/addOrderEvaluate";

    /**
     * 按订单开票界面
     */
    public static final String queryNotInvoiceMissionList = BASEURL + "/api/bill/queryNotInvoiceMissionList";

    /**
     * 历史订单
     * /api/bill/queryBillInvoice
     */
    public static final String queryBillInvoice = BASEURL + "/api/bill/queryBillInvoice";

    /**
     * 更新用户头像
     */
    public static final String updateLogo = BASEURL + "/api/user/updateLogo";
    /**
     * 更新用户信息
     */
    public static final String updateUserInfo = BASEURL + "/api/user/updateUserInfo";

    /**
     * 接单接口
     */
    public static final String addMissionIntention = BASEURL + "/api/intention/addMissionIntention";
    /**
     * 接单接口
     */
    public static final String updateMisInviteStatus = BASEURL + "/api/misInvite/updateMisInviteStatus";


    /**
     * 地图查询任务列表
     */
    public static final String queryByXYMap = BASEURL + "/api/mission/queryByXYMap";

    /**
     * 开票详情
     */
    public static final String addBillInvoice = BASEURL + "/api/bill/addBillInvoice";

    /**
     * 雇员详情
     */
    public static final String queryUserMisIntentionInfo = BASEURL + "/api/user/queryUserMisIntentionInfo";

    /**
     * 未处理雇员列表页
     */
    public static final String queryIntentionUser = BASEURL + "/api/user/queryIntentionUser";

    /**
     * 雇主同意或拒绝任务合作意向（雇主详情页)
     */
    public static final String updateStatus = BASEURL + "/api/intention/updateStatus";

    /**
     * 查看我的钱包余额---过时
     */
    public static final String queryBalance_obsolete = BASEURL + "/api/bill/queryBalance";

    /**
     * 查看我的钱包余额
     */
    public static final String queryBalance = BASEURL + "/api/userAccountRest/findOrangeAccount";

    /**
     * 充值
     */
    public static final String accountRecharge_obsolete = BASEURL + "/api/bill/accountRecharge";
    /**
     * 充值
     */
    public static final String accountRecharge = BASEURL + "/api/userAccountRest/recharge";

    /**
     * 提现
     */
    public static final String withdraw_obsolete = BASEURL + "/api/bill/withdraw";
    /**
     * 提现
     */
    public static final String withdraw = BASEURL + "/api/userAccountRest/withDraw";

    /**
     * 查看余额明细
     */
    public static final String queryBalanceRecode = BASEURL + "/api/bill/queryBalanceRecode";

    /**
     * 正在路上界面——完成订单/终止订单（协议终止/强制终止）
     */
    public static final String endComplete = BASEURL + "/api/ord/order/balance";

    /**
     * 添加评价
     */
    public static final String Evaluate = BASEURL + "/api/ord/add/evaluate";

    /**
     * 删除任务
     */
    public static final String Mission = BASEURL + "/api/mission/delete/mission";

    /**
     * 更新订单状态
     */
    public static final String OrderUpdateState = BASEURL + "/api/ord/update/status";

    /**
     * 根据订单Id获取雇主详情
     */
    public static final String queryEmployerInfo = BASEURL + "/api/user/queryEmployerInfo";

    /**
     * 余额不足去支付接口
     */
    public static final String payMission = BASEURL + "/api/mission/payMission";

    /**
     * 用户注册协议等web请求url  http://dlg.gongren.com/api/html/file?type=3
     */
    public static final String webURL = BASEURL + "/api/html/file?type=";

    /**
     * 雇主已完成界面接口
     */
    public static final String queryBillByMisId = BASEURL + "/api/bill/queryBillByMisId";

    /**
     * 雇主已完成界面接口
     */
    public static final String updateMissionEndByMisIds = BASEURL + "/api/mission/updateMissionEndByMisIds";

    /**
     * 雇员已接单界面接口
     */
    public static final String queryMisIntentionByUserId = BASEURL + "/api/intention/queryMisIntentionByUserId";

    /**
     * 查询接单范围
     */
    public static final String queryUserOrdersRange = BASEURL + "/api/user/queryUserOrdersRange";

    /**
     * 查询个人信息，用来开启人工服务
     */
    public static final String queryUserInfoToService = BASEURL + "/api/user/queryUserInfoToService";

    /**
     * 查询实名认证
     */
    public static final String queryRealNameInfo = BASEURL + "/api/user/queryRealNameInfo";

    /**
     * 更新实名认证
     */
    public static final String updateRealName = BASEURL + "/api/user/updateRealName";
    /**
     * 地图界面中介查询订单
     */
    public static final String BindingBankCards = BASEURL + "/api/billPay/bind";
    /**
     * 根据卡号查询所有银行
     */
    public static final String QueryBankBinList = BASEURL + "/api/bill/queryBankCardInfo";

    /**
     * 新增用户支付绑定
     */
    public static final String payBindRest = BASEURL + "/api/payBindRest/add";

    /**
     * 解绑支付宝、银行卡---过时
     */
    public static final String unbind_obsolete = BASEURL + "/api/billPay/unbind";

    /**
     * 解绑支付宝、银行卡
     */
    public static final String unbind = BASEURL + "/api/payBindRest/delete";

    /**
     * 查询支付宝、银行卡列表---过时
     */
    public static final String queryBindInfoList_obsolete = BASEURL + "/api/billPay/queryBindInfoList";
    /**
     * 查询支付宝、银行卡列表
     */
    public static final String queryBindInfoList = BASEURL + "/api/payBindRest/queryList";

    /**
     * 得到评价对象的名字
     */
    public static final String getEvaluteTag = BASEURL + "/api/evaluate-item/list";

    /**
     * 活动查询
     */
    public static final String querySysActivity = BASEURL + "/api/sysGencode/querySysActivity";
    /**
     * 首页活动页
     */
    public static final String ActivityByUser = BASEURL + "/api/activity/query/activityByUser";

    /**
     * 已完成订单详情的账单明细
     */
    public static final String queryBillDetailsByMisId = BASEURL + "/api/bill/queryBillDetailsByMisId";

    /**
     * 获取账单明细列表
     */
    public static final String findBillDetailList = BASEURL + "/api/billDetailRest/findBillDetailPageByUserIdAndStatus";
    /**
     * 获取账单明细列表详情
     */
    public static final String findBillDetailInfo = BASEURL + "/api/billDetailRest/findBillDetailInfoByNumber";
    /**
     * 雇主支付界面激光通过推来的激光显示不同的状态
     */
    public static final String queryAccUserJPushById = BASEURL + "/api/user/queryAccUserJPushById";

    /**
     * 获取数据字典列表
     */
    public static final String dictionaryRest = BASEURL + "/api/dictionaryRest/queryGroup";

    /**
     * 根据首字母获取城市
     */
    public static final String querySysAreaByClassify = BASEURL + "/api/sysGencode/querySysAreaByClassify";
    /**
     * 获取发现查询条件
     */
    public static final String queryDiscoverCommon = BASEURL + "/api/sysGencode/queryDiscoverCommon";
    /**
     * “发现”任务列表
     */
    public static final String queryDiscoverMission = BASEURL + "/api/mission/queryDiscoverMission";

    /**
     * 查看任务详情html地址
     */
    public static final String queryDetailPageInfo = BASEURL + "/api/mission/queryDetailPageInfo";

    /**
     * 添加意见反馈
     */
    public static final String addSuggestions = BASEURL + "/api/suggestions/addSuggestions";

    /**
     * 添加任务模板
     */
    public static final String addMissionModel = BASEURL + "/api/mission/addMissionModel";

    /**
     * 查询任务模板列表
     */
    public static final String queryMissionModelList = BASEURL + "/api/mission/queryMissionModelList";

    /**
     * 批量删除任务模板
     */
    public static final String deleteMissionModel = BASEURL + "/api/mission/deleteMissionModel";

    /**
     * 查询任务模板详情
     */
    public static final String queryMissionModelDetail = BASEURL + "/api/mission/queryMissionModelDetail";

    /**
     * 获取热门工种
     */
    public static final String querySysPositionHot = BASEURL + "/api/sysGencode/querySysPositionHot";

    /**
     * 打卡接口
     */
    public static final String updateOrdWorkingRecode = BASEURL + "/api/working/updateOrdWorkingRecode";

    /**
     * 收工打卡
     */
    public static final String overCard = BASEURL + "/api/working/updateOrdWorkingRecode";

    /**
     * 雇员端已拒绝批量删除
     */
    public static final String deleteRefuseOrder = BASEURL + "/api/ord/deleteRefuseOrder";

    /**
     * 雇主端草稿箱批量删除
     */
    public static final String deleteMissionDraft = BASEURL + "/api/mission/deleteMissionDraft";

    /**
     * 分享的标题与内容
     */
    public static final String shareContent = BASEURL + "/api/html/shareContent";

    /**
     * 微信绑定
     */
    public static final String BandingWX = BASEURL + "/api/payBindRest/add";

    /**
     * 获取本月提现次数
     */
    public static final String withdrawNum = BASEURL + "/api/billDetailRest/findUserMonthWithdrawalsNumber";

    /**
     * 添加常用地址
     */
    public static final String addAddress = BASEURL + "/api/user/address/add";

    /**
     * 查询地址列表
     */
    public static final String queryAddressList = BASEURL + "/api/user/address/queryAddressList";

    /**
     * 删除地址
     */
    public static final String deleteAddress = BASEURL + "/api/user/address/delete";

    /**
     * 设置地址类型
     */
    public static final String setType = BASEURL + "/api/user/address/setType";

    /**
     * 查询用户家和公司的地址
     */
    public static final String queryComAndHomeAddrByUserId = BASEURL + "/api/user/address/queryComAndHomeAddrByUserId";


    /**
     * 登录注册时候验证码的校验
     */
    public static final String validateMobileAndCode = BASEURL + "/oapi/user/validateMobileAndCode";

    /**
     * 查询雇员需求列表
     */
    public static final String queryWorkerNeedList = BASEURL + "/api/emplRequirement/queryListByUserId";

    /**
     * 雇主查询我的雇员列表
     */
    public static final String queryHistoryEmployeeByUserId = BASEURL + "/api/user/queryHistoryEmployeeByUserId";
    /**
     * 注册
     */
    public static final String unValidateRegister = BASEURL + "/oapi/user/unValidateRegister";
    /**
     * 查询证书列表
     */
    public static final String getQuerySkillsList =
            BASEURL + "/api/userFile/querySkillCertificateList";

    /**
     * 上传技能证书
     */
    public static final String addSkillCertificate =
            BASEURL + "/api/userFile/addSkillCertificate";

    /**
     * 查询发票列表---过时
     */
    public static final String queryInvoice =
            BASEURL + "/api/invoice/list";

    /**
     * 查询未开发票订单列表
     */
    public static final String billDetailRestList = BASEURL + "/api/billDetailRest/findBillDetailPageByUserIdAndStatus";

    /**
     * 所有搜索历史
     */
    public static final String jobConditionRestList = BASEURL + "/api/jobConditionRest/findByUserId";

    /**
     * 删除搜索历史
     */
    public static final String deleteJobConditionRest = BASEURL + "/api/jobConditionRest/deleteById";

    /**
     * 雇主列表查询
     */
    public static final String jobTaskRestEmployee = BASEURL + "/api/jobTaskRest/getJobTaskMapPageByEmployeeId";

    /**
     * 雇员零工列表查询
     */
    public static final String jobTaskRestJobTask = BASEURL + "/api/jobTaskRest/getJobTaskMapPage";
    /**
     * 新增零工条件
     */
    public static final String jobConditionRest = BASEURL + "/api/jobConditionRest/saveJobCondition";

    /**
     * 首页-根据雇员需求-列表
     */
    public static final String getJobTaskOfEmployeePage = BASEURL + "/api/jobTaskRest/getJobTaskOfEmployeePage";

    /**
     * 首页-根据雇员需求-我的零工-可接单列表
     */
    public static final String getJobTaskOfNotUsedPage = BASEURL + "/api/jobTaskRest/getJobTaskOfNotUsedPage";
    /**
     * 根据任务和订单状态查询订单列表
     */
    public static final String findByJobIdAndEmployerList = BASEURL + "/api/orderRest/findByJobIdAndEmployerIdAndStatus";
    /**
     * 首页-根据雇员需求-邀请
     */
    public static final String getJobTaskAskByEmployee = BASEURL + "/api/jobTaskRest/getJobTaskAskByEmployeeId";


    /**
     * 查询发票历史列表
     */
    public static final String queryInvoiceList =
            BASEURL + "/api/invoiceRest/queryInvoice";

    /**
     * 新增发票---过时
     */
    public static final String addInvoice_obsolete =
            BASEURL + "/api/invoice/add";

    /**
     * 新增发票
     */
    public static final String invoiceRest_addInvoice =
            BASEURL + "/api/invoiceRest/addInvoice";
    /**
     * 修改发票
     */
    public static final String updateInvoice =
            BASEURL + "/api/invoice/update";
    /**
     * 删除发票
     */
    public static final String deleteInvoice =
            BASEURL + "/api/invoice/delete";
    /**
     * 查看发票详情---过时
     */
    public static final String getInvoice_obsolete =
            BASEURL + "/api/invoice/get";
    /**
     * 查看发票模版
     */
    public static final String GETINVOICETEMPLATE =
            BASEURL + "/api/invoiceInformationRest/queryList";

    /**
     * 查看发票详情
     */
    public static final String getInvoiceRest =
            BASEURL + "/api/invoiceRest/findById";

    /**
     * 根据发票ID查询账单列表
     */
    public static final String queryByInvociceId =
            BASEURL + "/api/invoiceRest/queryByInvoiceId";

    /**
     * 新增雇员需求
     */
    public static final String emplRequirement =
            BASEURL + "/api/emplRequirement/add";
    /**
     * 更新最高学历
     */
    public static final String updateHighestDegreeCertificate = BASEURL + "/api/user/updateHighestDegreeCertificate";

    /**
     * 删除雇员需求返回结果
     */
    public static final String deleteByIds = BASEURL + "/api/emplRequirement/deleteByIds";

    /**
     * 查询雇员需求列表
     */
    public static final String queryListByUserId = BASEURL + "/api/emplRequirement/queryListByUserId";

    /**
     * 雇主获取自己的前三个任务
     */
    public static final String queryThreeMission = BASEURL + "/api/mission/queryThreeMission";

    /**
     * 雇员首页地图任务统计数量及按雇员需求筛选
     */
    public static final String queryMissionHomeInfo = BASEURL + "/api/mission/queryMissionHomeInfo";

    /**
     * 雇主首页地图--雇员需求数量及按雇主需求筛选
     */
    public static final String queryUserHome = BASEURL + "/api/user/queryUserHome";
    /**
     * 打卡记录
     */
    public static final String queryOrdWorkingRecode = BASEURL + "/api/working/queryOrdWorkingRecode";

    /**
     * 雇员首页地图--游客模式下查询任务
     */
    public static final String queryMissionHomeInfoByTourist = BASEURL + "/api/mission/queryMissionHomeInfoByTourist";

    /**
     * 查询雇主邀请发布列表
     */
    public static final String getQueryYaoQing = BASEURL + "/api/misInvite/queryMisInviteListByCreateId";

    /**
     * 查询雇员邀请列表
     */
    public static final String getQueryMyYaoQing = BASEURL + "/api/misInvite/queryMisInviteListByUserId";

    /**
     * 雇主添加邀请
     */
    public static final String addYaoQing = BASEURL + "/api/misInvite/add";
    /**
     * 雇员详情
     */
    public static final String employeeDetail = BASEURL + "/h5/employee/detail/";

    /**
     * 雇主详情
     */
    public static final String employerDetail = BASEURL + "/h5/employer/detail/";

    /**
     * 删除技能证书
     */
    public static final String delateSkills = BASEURL + "/api/userFile/deleteSkillCertificate";
    /**
     * 雇主取消订单
     */
    public static final String cancelMission = BASEURL + "/api/mission/addMission";

    /**
     * 修改技能证书
     */
    public static final String updateSkills = BASEURL + "/api/userFile/updateSkillCertificate";

    /**
     * 根据code查询系统数据
     */
    public static final String getQuerySystemDate = BASEURL + "/api/sysGencode/querySysGencode";

    /**
     * 保存实名认证信息
     */
    public static final String saveAuthenticateInfo = BASEURL + "/api/user/updateRealName";

    /**
     * 查询实名认证信息
     */
    public static final String queryAuthenticateInfo = BASEURL + "/api/user/queryRealNameInfo";

    /**
     * 撤单
     */
    public static final String cancelMisIntention = BASEURL + "/api/intention/cancelMisIntention";
    /**
     * 雇主已发布、已成交取消订单的接口(8,7)
     */
    public static final String cancelOrder = BASEURL + "/api/mission/updateMissionEndByMisIds";
    /**
     * 点击扣款还是成功
     */
    public static final String status = BASEURL + "/api/ord/update/status";

    /**
     * 查询橙度
     */
    public static final String queryChengxinScore = BASEURL + "/api/trust/queryChengxinScore";

    /**
     * 添加支付密码---过时
     */
    public static final String addPayPassword_obsolete = BASEURL + "/api/payPwd/addPayPassword";

    /**
     * 添加支付密码
     */
    public static final String addPayPassword = BASEURL + "/api/payPasswordRest/add";

    /**
     * 验证支付密码
     */
    public static final String validate = BASEURL + "/api/payPwd/validate";

    /**
     * 修改支付密码---过时
     */
    public static final String updatePayPwd_obsolete = BASEURL + "/api/payPwd/updatePayPwd";
    /**
     * 修改支付密码
     */
    public static final String updatePayPwd = BASEURL + "/api/payPasswordRest/update";
    /**
     * 提交兑换码--过时
     */
    public static final String redeem_obsolete = BASEURL + "/api/redeemCode/redeem";

    //----------------------------------------
    //新版的接口
    /**
     * 提交兑换码
     */
    public static final String redeem = BASEURL + "/api/exchangeCodeRest/exchangeCodeNumber";
    /**
     * 查询个人中心用户资料
     */
    public static final String get_user_information = BASEURL + "/api/userRest/queryUserDetail";
    /**
     * 更新个人中心用户资料
     */
    public static final String update_user_information = BASEURL + "/api/userRest/updatePersonalUser";
    /**
     * 更新个人中心用户资料（主表）
     */
    public static final String update_user_information_main = BASEURL + "/api/userRest/updateUser";
    /**
     * 上传用户头像
     */
    public static final String upload_user_head_icon = BASEURL + "/api/userRest/uploadPersonalUserLogo";
    /**
     * 身份证内容识别
     */
    public static final String ID_CARD_DISCEM = BASEURL + "/api/idCardRest/ocr";
    /**
     * 身份证验证
     */
    public static final String ID_CARD_identification = BASEURL + "/api/idCardRest/certification";
    /**
     * 发布的零工列表
     */
    public static final String GUZHU_LINGONG_LIST = BASEURL + "/api/jobTaskRest/getJobTaskOfMasterPage";

    public static final String GUZHU_LINGONG_ORDER_LIST = BASEURL + "/api/orderRest/findByJobIdAndEmployerId";
    /**
     * 发布零工
     */
    public static final String RELEASE_JOB = BASEURL + "/api/jobTaskRest/saveJobTask";
    /**
     * 编辑零工
     * /api/jobTaskRestupdateJobTaskById
     */
    public static final String EDIT_RELEASE_JOB = BASEURL + "/api/jobTaskRest/updateJobTaskById";
    /**
     * 查询系统消息
     * liukui 2014/04/24
     */
    public static final String userSystemMessage = BASEURL + "/api/userMessageRest/queryList";
    /**
     * 消息详情
     * liukui 2017/04/24
     */
    public static final String messageListDetail = BASEURL + "/api/userMessageRest/query";
    /**
     * 消息详情
     * liukui 2017/04/24
     */
    public static final String messageDetail = BASEURL + "/api/messageRest/findMessageDetail";
    /**
     * 更新消息为已读
     */
    public static final String messageUpdate = BASEURL + "/api/userMessageRest/update";
    /**
     * 获取未读消息总数
     */
    public static final String unReadMessageCounts = BASEURL + "/api/userMessageRest/queryMessageNoReadCount";
    /**
     * 获取最新未读消息列表
     */
    public static final String getLatestUnReadMessageList = BASEURL + "/api/messageRest/queryMessageNoReadAndLastOne";
    /**
     * 获取任务的时间
     */
    public static final String get_trask_time = BASEURL + "/api/orderRest/findRemainingTime";
    /**
     * 获取正在进行的任务订单信息
     */
    public static final String get_doing_task_detail = BASEURL + "/api/orderRest/findListByEmployeeIdOrBusinessNumber";
    /**
     * 订单按钮的操作信息
     */
    public static final String get_task_buttons = BASEURL + "/api/orderFlowConfigRest/findByBusinessNumber";
    /**
     * 任务订单操作按钮请求
     */
    public static final String post_task_button_event = BASEURL + "/api/orderRest/updateOrderOperaStatus";
    /**
     * 查询当前进行中的订单
     */
    public static final String QUERY_ORDER = BASEURL + "/api/orderRest/findFirstOrder";

    /**
     * 查询个人信息
     */
    public static final String QUERY_PERSONAL_INFO = BASEURL + "/api/userRest/queryUserDetail";

    /**
     * 操作按钮展示
     */
    public static final String ACTION_BUTTON = BASEURL + "/api/orderFlowConfigRest/findByBusinessNumber";
    /**
     * 订单结算
     */
    public static final String ORDER_SETTLEMENT = BASEURL + "/api/userAccountRest/orderSettlement";
    /**
     * 根据订单id查询订单详情
     */
    public static final String get_task_detail_by_id = BASEURL + "/api/orderRest/findByBusinessNumber";
    /**
     * 批量操作订单
     */
    public static final String BATCH_MODIFICATION = BASEURL + "/api/orderRest/updateOrderOperaStatusBatch";
    /**
     * 发布地址
     */
    public static final String ADDRESS = BASEURL + "/api/districtAreaRest/findArearByParentId";

    /**
     * 抢单
     */
    public static final String post_strive_for_order = BASEURL + "/api/orderRest/saveOrder";
    /**
     * 取消订单
     */
    public static final String CANCEL_BUSSINESS = BASEURL + "/api/orderCancelRest/cancel";

    /**
     * 查询是否存在支付密码
     */
    public static final String CHECK_PAYMENT_PASS = BASEURL + "/api/payPasswordRest/checkSet";
    /**
     * 删除订单
     */
    public static final String DELETE_ORDER = BASEURL + "/api/orderRest/deleteOrderByBusiness";
    /**
     * 基础页面H5
     */
    public static final String BASEURL_H5 = BASEURL + "/api/appHtmlRestApi/redirectHtml";
    /**
     * 分享内容获取
     */
    public static final String SHARE_DATA = BASEURL + "/api/jobTaskRest/findJobTaskShareToWeiXin";
    /**
     * 删除订单任务 雇主
     */
    public static final String DELETE_BOSS_ORDER = BASEURL + "/api/jobTaskRest/deleteJobTaskById";
    /**
     * 真我
     */
    public static final String ZHEN_WO=BASEURL+"/api/appHtmlRestApi/redirectHtml?";
    /**
     * 积分商城
     */
    public static final String INTEGRAL_MALL=BASEURL+"/api/commodityRest/list?";

    /**
     * 查询最新版本
     */
    public static final String CHECK_VERSION = BASEURL+"/api/dictionaryRest/getNewVersion";
    /**
     * 添加服务
     */
    public static final String ADDSERVICE = BASEURL+"/api/jobServiceRest/saveOrUpdateJobService";
    /**
     * 查询零工服务列表
     */
    public static final String SELECT_SERVICELIST = BASEURL+"/api/jobServiceRest/findByUserId";
    /**
     *1.7.1.5（REST）删除零工服务
     */
    public static final String DELETE_SERVICE = BASEURL+"/api/jobServiceRest/deleteById";
    /**
     * 银行卡验证
     * */
    public static final String BANK_CARD_CERTIFICATE = BASEURL+"/api/payBindRest/bankCardOcr";
    /**
     * 预约
     */
    public static final String WILL_SERVICE = BASEURL+"/api/jobServiceRest/saveJobTask";
    /**
     * 服务分享接口
     */
    public static final String SERVICE_SHARE = BASEURL+"/api/jobServiceRest/findJobServiceShareToWeiXin";
    /**
     * 服务详情
     */
    public static final String SERVICE_DETAIL = BASEURL+"/api/jobServiceRest/findById";
}
