package gongren.com.dlg.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import java.util.List;

import aym.util.json.JsonMap;
import butterknife.Bind;
import butterknife.ButterKnife;
import gongren.com.dlg.R;
import gongren.com.dlg.activity.WebUtilsActivity;
import gongren.com.dlg.volleyUtils.GetDataConfing;

/**
 * 适配系统消息ListView的Adapter
 */
public class ActivityExtensionAdapter extends SimpleBaseAdapter<JsonMap<String, Object>> {
    private  Handler handler;
    private Context mContext ;
    public ActivityExtensionAdapter(Context context, List<JsonMap<String, Object>> list, Handler handler) {
        super(context, list);
        mContext = context ;
        this.handler = handler ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list_activity_extension, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        if(position==0){
            viewHolder.rl_day_bg.setVisibility(View.VISIBLE);
        }else{
            viewHolder.rl_day_bg.setVisibility(View.GONE);
        }
        viewHolder.tv_news_time.setText("12:00");

        final JsonMap<String, Object> jsonMap = list.get(position);
        Glide.with(context).load(jsonMap.getString("imageUrl"))
                .priority(Priority.HIGH).error(R.mipmap.icon_find_banner)
                .into(viewHolder.img_activity_image);

        viewHolder.tv_system_news_title.setText(jsonMap.getString("content"));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Message mesg = new Message();
//                mesg.what = IntegerUtils.MESSAGE_LIST_DETAIL;
//                mesg.obj = jsonMap;
//                handler.sendMessage(mesg);
                Intent agreementIntent = new Intent(context, WebUtilsActivity.class);
                agreementIntent.putExtra("functionTitle","活动推广");
                agreementIntent.putExtra("contentUrl", GetDataConfing.BASEURL_H5);
                agreementIntent.putExtra("type","3");
                mContext.startActivity(agreementIntent);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.rl_day_bg)
        RelativeLayout rl_day_bg;
        @Bind(R.id.tv_news_time)
        TextView tv_news_time;
        @Bind(R.id.img_activity_image)
        ImageView img_activity_image;
        @Bind(R.id.rl_news_info)
        RelativeLayout rl_news_info;
        @Bind(R.id.tv_system_news_title)
        TextView tv_system_news_title;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
