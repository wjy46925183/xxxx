package spring.update;

import android.app.Activity;

import spring.update.business.DownloadWorker;
import spring.update.business.UpdateWorker;
import spring.update.callback.ActivityReplaceCB;
import spring.update.callback.UpdateCheckCB;
import spring.update.callback.UpdateDownloadCB;
import spring.update.creator.ApkFileCreator;
import spring.update.creator.DialogCreator;
import spring.update.creator.DownloadCreator;
import spring.update.creator.InstallCreator;
import spring.update.model.CheckEntity;
import spring.update.model.UpdateChecker;
import spring.update.model.UpdateParser;
import spring.update.strategy.UpdateStrategy;


/**
 *
 * @author lzh
 */
public class UpdateBuilder {

    /**
     * @see UpdateWorker
     */
    private UpdateWorker checkWorker;
    private DownloadWorker downloadWorker;
    private UpdateCheckCB checkCB;
    private UpdateDownloadCB downloadCB;
    private String url;
    private CheckEntity entity;
    private UpdateStrategy strategy;
    private DialogCreator updateDialogCreator;
    private InstallCreator installDialogCreator;
    private DownloadCreator downloadDialogCreator;
    private UpdateParser jsonParser;
    private ApkFileCreator fileCreator;
    private UpdateChecker updateChecker;
    private ActivityReplaceCB replaceCB;

    public static UpdateBuilder create() {
        return new UpdateBuilder();
    }

    public UpdateBuilder url(String url) {
        this.entity = new CheckEntity().setUrl(url);
        return this;
    }

    public UpdateBuilder checkEntity (CheckEntity entity) {
        this.entity = entity;
        return this;
    }

    public UpdateBuilder updateChecker (UpdateChecker checker) {
        this.updateChecker = checker;
        return this;
    }

    public UpdateBuilder checkWorker(UpdateWorker checkWorker) {
        this.checkWorker = checkWorker;
        return this;
    }

    public UpdateBuilder downloadWorker(DownloadWorker downloadWorker) {
        this.downloadWorker = downloadWorker;
        return this;
    }

    public UpdateBuilder downloadCB(UpdateDownloadCB downloadCB) {
        this.downloadCB = downloadCB;
        return this;
    }

    public UpdateBuilder checkCB (UpdateCheckCB checkCB) {
        this.checkCB = checkCB;
        return this;
    }

    public UpdateBuilder jsonParser (UpdateParser jsonParser) {
        this.jsonParser = jsonParser;
        return this;
    }

    public UpdateBuilder fileCreator (ApkFileCreator fileCreator) {
        this.fileCreator = fileCreator;
        return this;
    }

    public UpdateBuilder downloadDialogCreator (DownloadCreator downloadDialogCreator) {
        this.downloadDialogCreator = downloadDialogCreator;
        return this;
    }

    public UpdateBuilder installDialogCreator (InstallCreator installDialogCreator) {
        this.installDialogCreator = installDialogCreator;
        return this;
    }

    public UpdateBuilder updateDialogCreator(DialogCreator updateDialogCreator) {
        this.updateDialogCreator = updateDialogCreator;
        return this;
    }

    public UpdateBuilder strategy(UpdateStrategy strategy) {
        this.strategy = strategy;
        return this;
    }

    public UpdateBuilder replaceCB(ActivityReplaceCB replaceCB) {
        this.replaceCB = replaceCB;
        return this;
    }

    public void check(Activity activity) {
        Updater.getInstance().checkUpdate(activity, this);
    }

    public UpdateStrategy getStrategy() {
        if (strategy == null) {
            strategy = UpdateConfig.getConfig().getStrategy();
        }
        return strategy;
    }

    public CheckEntity getCheckEntity () {
        if (this.entity == null) {
            this.entity = UpdateConfig.getConfig().getCheckEntity();
        }
        return this.entity;
    }

    public UpdateChecker getUpdateChecker() {
        if (updateChecker == null) {
            updateChecker = UpdateConfig.getConfig().getUpdateChecker();
        }
        return updateChecker;
    }

    public DialogCreator getUpdateDialogCreator() {
        if (updateDialogCreator == null) {
            updateDialogCreator = UpdateConfig.getConfig().getUpdateDialogCreator();
        }
        return updateDialogCreator;
    }

    public InstallCreator getInstallDialogCreator() {
        if (installDialogCreator == null) {
            installDialogCreator = UpdateConfig.getConfig().getInstallDialogCreator();
        }
        return installDialogCreator;
    }

    public DownloadCreator getDownloadDialogCreator() {
        if (downloadDialogCreator == null) {
            downloadDialogCreator = UpdateConfig.getConfig().getDownloadDialogCreator();
        }
        return downloadDialogCreator;
    }

    public UpdateParser getJsonParser() {
        if (jsonParser == null) {
            jsonParser = UpdateConfig.getConfig().getJsonParser();
        }
        return jsonParser;
    }

    public UpdateWorker getCheckWorker() {
        if (checkWorker == null) {
            checkWorker = UpdateConfig.getConfig().getCheckWorker();
        }
        return checkWorker;
    }

    public DownloadWorker getDownloadWorker() {
        if (downloadWorker == null) {
            downloadWorker = UpdateConfig.getConfig().getDownloadWorker();
        }
        return downloadWorker;
    }

    public ApkFileCreator getFileCreator() {
        if (fileCreator == null) {
            fileCreator = UpdateConfig.getConfig().getFileCreator();
        }
        return fileCreator;
    }

    public UpdateCheckCB getCheckCB() {
        if (checkCB == null) {
            checkCB = UpdateConfig.getConfig().getCheckCB();
        }
        return checkCB;
    }

    public UpdateDownloadCB getDownloadCB() {
        if (downloadCB == null) {
            downloadCB = UpdateConfig.getConfig().getDownloadCB();
        }
        return downloadCB;
    }

    public ActivityReplaceCB getReplaceCB () {
        return replaceCB;
    }
}
