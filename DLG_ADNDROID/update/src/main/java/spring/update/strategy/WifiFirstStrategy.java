package spring.update.strategy;


import spring.update.model.Update;
import spring.update.util.NetworkUtil;

/**
 * @author Administrator
 */
public class WifiFirstStrategy implements UpdateStrategy {

    boolean isWifi;

    @Override
    public boolean isShowUpdateDialog(Update update) {
        isWifi = NetworkUtil.isConnectedByWifi();
        return !isWifi;
    }

    @Override
    public boolean isAutoInstall() {
        return !isWifi;
    }

    @Override
    public boolean isShowDownloadDialog() {
        return !isWifi;
    }
}
