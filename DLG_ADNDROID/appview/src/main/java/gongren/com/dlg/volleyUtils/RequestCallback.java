package gongren.com.dlg.volleyUtils;

/**
 * Created by Administrator on 2017/4/18 0018.
 */

public interface RequestCallback<T> {
    void onSuccess(T t);
    void onError(String mag);
}
