package gongren.com.dlg.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.common.view.iamge.TouchImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gongren.com.dlg.R;

/**
 * 作者：wangdakuan
 * 主要功能：大图查看页面
 * 创建时间：2017/6/8 18:28
 */
public class ImageViewActivity extends BaseActivity{

    public static final String IMAGE_URL = "iamge_url"; //图片地址

    @Bind(R.id.page_layout)
    RelativeLayout pageLayout;
    @Bind(R.id.iamge_view)
    TouchImageView imageView; //图片显示

    private String imageUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_iamge);
        ButterKnife.bind(this);
        imageUrl = getIntent().getStringExtra(IMAGE_URL);
        Glide.with(this).load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        imageView.setImageDrawable(resource);
                    }
                });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @OnClick({R.id.page_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.page_layout:
                //返回
                finish();
                break;
        }
    }
}
