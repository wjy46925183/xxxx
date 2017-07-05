package spring.update.model;


import spring.update.UpdateConfig;
import spring.update.util.InstallUtil;
import spring.update.util.UpdatePreference;

/**
 * Created by Administrator on 2016/6/20.
 */
public class DefaultChecker implements UpdateChecker {
    @Override
    public boolean check(Update update) {
        try {
            int curVersion = InstallUtil.getApkVersion(UpdateConfig.getConfig().getContext());
            if (update.getVersionCode() > curVersion &&
                    (update.isForced()||
                    !UpdatePreference.getIgnoreVersions().contains(String.valueOf(update.getVersionCode())))) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
