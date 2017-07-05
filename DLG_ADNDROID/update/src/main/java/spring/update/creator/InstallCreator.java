package spring.update.creator;

import android.app.Activity;
import android.app.Dialog;

import spring.update.UpdateConfig;
import spring.update.callback.UpdateCheckCB;
import spring.update.model.Update;
import spring.update.util.InstallUtil;
import spring.update.util.Recycler;
import spring.update.util.UpdatePreference;


/**
 *
 * @author lzh
 */
public abstract class InstallCreator implements Recycler.Recycleable {

    private UpdateCheckCB checkCB;

    public void setCheckCB(UpdateCheckCB checkCB) {
        this.checkCB = checkCB;
    }

    public abstract Dialog create(Update update, String path, Activity activity);

    /**
     * request to install this apk file
     * @param filename the absolutely file name that downloaded
     */
    public void sendToInstall(String filename) {
        InstallUtil.installApk(UpdateConfig.getConfig().getContext(),filename);
        Recycler.release(this);
    }

    /**
     * request cancel install action
     */
    public void sendUserCancel() {
        if (this.checkCB != null) {
            this.checkCB.onUserCancel();
        }

        Recycler.release(this);
    }

    public void sendCheckIgnore(Update update) {
        if (this.checkCB != null) {
            this.checkCB.onCheckIgnore(update);
        }
        UpdatePreference.saveIgnoreVersion(update.getVersionCode());
        Recycler.release(this);
    }

    @Override
    public void release() {
        this.checkCB = null;
    }
}
