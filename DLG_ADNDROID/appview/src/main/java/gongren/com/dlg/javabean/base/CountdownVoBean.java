package gongren.com.dlg.javabean.base;

import java.io.Serializable;

/**
 * 作者：wangdakuan
 * 主要功能：倒计时与地理信息
 * 创建时间：2017/5/25 12:54
 */
public class CountdownVoBean implements Serializable{
    /**
     * thisDate : 1494827265225
     * remainingTime : 4334775remainingTime
     * mapTipsText : 后开工
     * otherMapTipsText : 请到地图上蓝色范围内进行
     */

    public long thisDate;
    public long remainingTime;
    public String mapTipsText;
    public String otherMapTipsText;
}
