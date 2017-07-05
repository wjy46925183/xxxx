package gongren.com.dlg.utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.common.view.convenientbanner.holder.Holder;

public class NetworkImageHolderView implements Holder<String> {
    private ImageView imageView;
    @Override
    public View createView(Context context) {
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context,int position, String data) {
//        imageView.setImageResource(R.mipmap.icon_find_banner);
        Glide.with(context)
                .load(data)
                .error(demo.java.com.common.R.mipmap.picture_loading)
                .placeholder(demo.java.com.common.R.mipmap.picture_loading)
                .into(imageView);
    }
}
