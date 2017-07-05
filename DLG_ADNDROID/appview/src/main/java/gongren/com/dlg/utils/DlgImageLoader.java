package gongren.com.dlg.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import gongren.com.dlg.R;

/**
 * Created by Administrator on 2017/4/24 0024.
 */

public class DlgImageLoader {


    public static void loadImage(Context context, String url, ImageView imageView){
        Glide.with(context)
                .load(url)
                .placeholder(R.mipmap.morentouxiang)
                .error(R.mipmap.morentouxiang)
                .dontAnimate()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }
}
