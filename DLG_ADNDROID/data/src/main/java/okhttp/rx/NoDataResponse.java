package okhttp.rx;

import java.io.Serializable;

/**
 * 作者：wangdakuan
 * 主要功能：没有返回如何数据的初始化Response
 * 创建时间：2016/12/26 16:21
 */
public class NoDataResponse  implements Serializable {

    private String msg; //提示信息
    private String sid; //
    private Integer code; //状态 0为成功 1

    public JsonResponse toJsonResponse() {
        JsonResponse response = new JsonResponse();
        response.setCode(getCode());
        response.setMsg(getMsg());
        response.setSid(getSid());
        return response;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
