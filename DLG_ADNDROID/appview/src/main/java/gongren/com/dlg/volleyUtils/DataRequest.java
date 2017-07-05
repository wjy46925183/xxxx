package gongren.com.dlg.volleyUtils;

import android.text.TextUtils;

import java.util.Map;

/**
 * Created by Jaelyn on 2015/12/3 0003.
 * 
 * 网络数据请求对象
 */
public class DataRequest {

	/**
	 * 调用时要传入的key
	 */
	public static final String Params_key = "Params_key";
	/**
	 * 请求的标记
	 */
	public int what;
	/**
	 * 服务器地址
	 */
	private String ip;
	/**
	 * 方法名字
	 */
	private String actionName;
	/**
	 * 请求参数
	 */
	private Map<String, Object> params;

	/**
	 * 网络返回数据
	 */
	private String responseData = "";

	/**
	 * 标记网络错误
	 */
	private boolean isNetError = false;

	public boolean isNetError() {
		return isNetError;
	}

	public void setNetError(boolean isNetError) {
		this.isNetError = isNetError;
	}

	/**
	 * 网络数据回调监听
	 */
	private ResponseDataCallback responseDataCallback;

	public ResponseDataCallback getResponseDataCallback() {
		return responseDataCallback;
	}

	public void setResponseDataCallback(
			ResponseDataCallback responseDataCallback) {
		this.responseDataCallback = responseDataCallback;
	}

	public static String getParams_key() {
		return Params_key;
	}

	public int getWhat() {
		return what;
	}

	public void setWhat(int what) {
		this.what = what;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getActionName() {
		return actionName;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getResponseData() {
		return responseData;
	}

	public void setResponseData(String responseData) {
		this.responseData = responseData;
	}

	/**
	 * 重写父类的获取信息的方法 当含有&lt;string> &lt;/string>时去掉前后的&lt;string> &lt;/string>
	 */
	public String getInfo() {
		if (TextUtils.isEmpty(responseData)) {
			int startIndex = responseData.indexOf("{");
			int endIndex = responseData.lastIndexOf("}");
			responseData = responseData.substring(startIndex, endIndex + 1);
		}
		return responseData;
	}
}
