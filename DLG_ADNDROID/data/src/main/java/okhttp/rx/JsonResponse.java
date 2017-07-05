package okhttp.rx;

import java.io.Serializable;

/**
 * 作者：wangdakuan
 * 主要功能：请求数据Response对象
 * 创建时间：2017/6/1 16:22
 */
public class JsonResponse<T> implements Serializable {

    private String msg; //提示信息
    private String sid; //
    private Integer code; //状态 0为成功 1
    private T data; //返回的数据

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
