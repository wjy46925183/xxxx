package spring.update;

import android.content.Context;
import android.text.TextUtils;

import spring.update.business.DefaultDownloadWorker;
import spring.update.business.DefaultUpdateWorker;
import spring.update.business.DownloadWorker;
import spring.update.business.UpdateWorker;
import spring.update.callback.ActivityReplaceCB;
import spring.update.callback.UpdateCheckCB;
import spring.update.callback.UpdateDownloadCB;
import spring.update.creator.ApkFileCreator;
import spring.update.creator.DefaultFileCreator;
import spring.update.creator.DefaultNeedDownloadCreator;
import spring.update.creator.DefaultNeedInstallCreator;
import spring.update.creator.DefaultNeedUpdateCreator;
import spring.update.creator.DialogCreator;
import spring.update.creator.DownloadCreator;
import spring.update.creator.InstallCreator;
import spring.update.model.CheckEntity;
import spring.update.model.DefaultChecker;
import spring.update.model.UpdateChecker;
import spring.update.model.UpdateParser;
import spring.update.strategy.UpdateStrategy;
import spring.update.strategy.WifiFirstStrategy;


/**
 * Global configs
 */
public class UpdateConfig {

    private Context context;

    /**
     * update task
     */
    private UpdateWorker checkWorker;

    /**
     * download task
     */
    private DownloadWorker downloadWorker;
    /**
     * The callback to receive update task info
     */
    private UpdateCheckCB checkCB;
    /**
     * The callback to receive download task info
     */
    private UpdateDownloadCB downloadCB;
//    /**
//     * The url will be used by {@link UpdateWorker} to access network
//     */
//    private String url;
    /**
     * The entity will be used by {@link UpdateWorker} to access network
     */
    private CheckEntity entity;
    /**
     * The strategy on update
     */
    private UpdateStrategy strategy;
    /**
     * To create update dialog when should be shown,according to
     */
    private DialogCreator updateDialogCreator;
    /**
     * To create install dialog when should be shown,according to {@link UpdateStrategy#isAutoInstall()}
     */
    private InstallCreator installDialogCreator;
    /**
     * To create download dialog when should be shown,according to {@link UpdateStrategy#isShowDownloadDialog()}
     */
    private DownloadCreator downloadDialogCreator;
    /**
     * The parser to parse response data form url to  instance
     */
    private UpdateParser jsonParser;
    /**
     * To create cache apk file name in download task:{@link DownloadWorker}
     */
    private ApkFileCreator fileCreator;
    /**
     * To check out whether or not there are a new version of apk should be updated
     */
    private UpdateChecker updateChecker;

    /**
     * To replace activity when current is finished or should shown dialog with a new activity
     */
    private ActivityReplaceCB replaceCB;

    private static UpdateConfig config;
    public static UpdateConfig getConfig() {
        if (config == null) {
            config = new UpdateConfig();
        }
        return config;
    }

    /**
     * Cache application context
     * @param context Activity context
     */
    UpdateConfig context (Context context) {
        if (this.context == null) {
            this.context = context.getApplicationContext();
        }
        return this;
    }

    /**
     * to see {@link UpdateConfig#url}
     */
    public UpdateConfig url(String url) {
        this.entity = new CheckEntity().setUrl(url);
        return this;
    }

    /**
     * @see UpdateConfig#entity
     */
    public UpdateConfig checkEntity (CheckEntity entity) {
        this.entity = entity;
        return this;
    }

    /**
     * To see {@link UpdateConfig#updateChecker}
     */
    public UpdateConfig updateChecker(UpdateChecker checker) {
        this.updateChecker = checker;
        return this;
    }

    /**
     * To see {@link UpdateConfig#checkWorker}
     */
    public UpdateConfig checkWorker(UpdateWorker checkWorker) {
        this.checkWorker = checkWorker;
        return this;
    }

    /**
     * To see {@link UpdateConfig#downloadWorker}
     */
    public UpdateConfig downloadWorker(DownloadWorker downloadWorker) {
        this.downloadWorker = downloadWorker;
        return this;
    }

    /**
     * To see {@link UpdateConfig#downloadCB}
     */
    public UpdateConfig downloadCB(UpdateDownloadCB downloadCB) {
        this.downloadCB = downloadCB;
        return this;
    }

    /**
     * To see {@link UpdateConfig#checkCB}
     */
    public UpdateConfig checkCB (UpdateCheckCB checkCB) {
        this.checkCB = checkCB;
        return this;
    }

    /**
     * To see {@link UpdateConfig#jsonParser}
     */
    public UpdateConfig jsonParser (UpdateParser jsonParser) {
        this.jsonParser = jsonParser;
        return this;
    }

    /**
     * To see {@link UpdateConfig#fileCreator}
     */
    public UpdateConfig fileCreator (ApkFileCreator fileCreator) {
        this.fileCreator = fileCreator;
        return this;
    }

    /**
     * To see {@link UpdateConfig#downloadDialogCreator}
     */
    public UpdateConfig downloadDialogCreator (DownloadCreator downloadDialogCreator) {
        this.downloadDialogCreator = downloadDialogCreator;
        return this;
    }

    /**
     * To see {@link UpdateConfig#installDialogCreator}
     */
    public UpdateConfig installDialogCreator (InstallCreator installDialogCreator) {
        this.installDialogCreator = installDialogCreator;
        return this;
    }

    /**
     * To see {@link UpdateConfig#updateDialogCreator}
     */
    public UpdateConfig updateDialogCreator(DialogCreator updateDialogCreator) {
        this.updateDialogCreator = updateDialogCreator;
        return this;
    }

    /**
     * To see {@link UpdateConfig#strategy}
     */
    public UpdateConfig strategy(UpdateStrategy strategy) {
         this.strategy = strategy;
        return this;
    }

    /**
     * @return To see {@link UpdateConfig#replaceCB}
     */
    public UpdateConfig replaceCB(ActivityReplaceCB replaceCB) {
        this.replaceCB = replaceCB;
        return this;
    }

    public Context getContext() {
        if (context == null) {
            throw new RuntimeException("should call UpdateConfig.install first");
        }
        return context;
    }

    /**
     * @return To see {@link UpdateConfig#strategy}
     */
    public UpdateStrategy getStrategy() {
        if (strategy == null) {
            strategy = new WifiFirstStrategy();
        }
        return strategy;
    }

//    /**
//     *
//     */
//    @Deprecated
//    public String getUrl() {
//        if (this.entity == null || TextUtils.isEmpty(this.entity.getUrl())) {
//            throw new IllegalArgumentException("url is null");
//        }
//        return this.entity.getUrl();
//    }

    /**
     * @return To see {@linkplain UpdateConfig#entity}
     */
    public CheckEntity getCheckEntity () {
        if (this.entity == null || TextUtils.isEmpty(this.entity.getUrl())) {
            throw new IllegalArgumentException("Do not set url in CheckEntity");
        }
        return this.entity;
    }

    /**
     * @return To see {@link UpdateConfig#updateDialogCreator}
     */
    public DialogCreator getUpdateDialogCreator() {
        if (updateDialogCreator == null) {
            updateDialogCreator = new DefaultNeedUpdateCreator();
        }
        return updateDialogCreator;
    }

    /**
     * @return To see {@link UpdateConfig#installDialogCreator}
     */
    public InstallCreator getInstallDialogCreator() {
        if (installDialogCreator == null) {
            installDialogCreator = new DefaultNeedInstallCreator();
        }
        return installDialogCreator;
    }

    /**
     * @return To see {@link UpdateConfig#updateChecker}
     */
    public UpdateChecker getUpdateChecker() {
        if (updateChecker == null) {
            updateChecker = new DefaultChecker();
        }
        return updateChecker;
    }

    /**
     * @return To see {@link UpdateConfig#downloadDialogCreator}
     */
    public DownloadCreator getDownloadDialogCreator() {
        if (downloadDialogCreator == null) {
            downloadDialogCreator = new DefaultNeedDownloadCreator();
        }
        return downloadDialogCreator;
    }

    /**
     * @return To see {@link UpdateConfig#jsonParser}
     */
    public UpdateParser getJsonParser() {
        if (jsonParser == null) {
            throw new IllegalStateException("update parser is null");
        }
        return jsonParser;
    }

    /**
     * @return To see {@link UpdateConfig#checkWorker}
     */
    public UpdateWorker getCheckWorker() {
        if (checkWorker == null) {
            checkWorker = new DefaultUpdateWorker();
        }
        return checkWorker;
    }

    /**
     * @return To see {@link UpdateConfig#downloadWorker}
     */
    public DownloadWorker getDownloadWorker() {
        if (downloadWorker == null) {
            downloadWorker = new DefaultDownloadWorker();
        }
        return downloadWorker;
    }

    /**
     * @return To see {@link UpdateConfig#fileCreator}
     */
    public ApkFileCreator getFileCreator() {
        if (fileCreator == null) {
            fileCreator = new DefaultFileCreator();
        }
        return fileCreator;
    }

    /**
     * @return To see {@link UpdateConfig#checkCB}
     */
    public UpdateCheckCB getCheckCB() {
        return checkCB;
    }

    /**
     * @return To see {@link UpdateConfig#downloadCB}
     */
    public UpdateDownloadCB getDownloadCB() {
        return downloadCB;
    }

    /**
     * @return To see {@link UpdateConfig#replaceCB}
     */
    public ActivityReplaceCB getReplaceCB () {
        return replaceCB;
    }
}

