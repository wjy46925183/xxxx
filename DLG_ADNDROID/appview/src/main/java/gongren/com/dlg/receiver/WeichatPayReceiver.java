package gongren.com.dlg.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by zq on 2016/10/18.
 */
public class WeichatPayReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int errCode = intent.getIntExtra("errCode", -1);
        Intent msgIntent = new Intent("com.gongren.weichatPay");
        msgIntent.putExtra("errCode", errCode);
        context.sendBroadcast(msgIntent);
    }
}
