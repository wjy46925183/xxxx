package gongren.com.dlg.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import aym.util.json.JsonMap;
import butterknife.Bind;
import butterknife.ButterKnife;
import gongren.com.dlg.R;
import gongren.com.dlg.activity.WebUtilsActivity;
import gongren.com.dlg.javabean.UserMessageModel;
import com.common.utils.DateUtils;
import gongren.com.dlg.volleyUtils.GetDataConfing;

/**
 * 推荐零工适配器
 *
 * liukui 2014/01/24
 */
public class JobRecomAdapter extends SimpleBaseAdapter<JsonMap<String, Object>> {
    private  Handler handler;
    private UserMessageModel userMessageModel;
    private Context mContext ;

    public JobRecomAdapter(Context context, List<JsonMap<String, Object>> list, Handler handler) {
        super(context, list);
        userMessageModel = new UserMessageModel();
        this.handler = handler ;
        mContext = context ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list_job_recom, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();
        final JsonMap<String, Object> jsonMap = list.get(position);
        showDate(viewHolder, position);
        viewHolder.tv_system_news_title.setText(jsonMap.getString("content"));
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Message mesg = new Message();
//                mesg.what = IntegerUtils.MESSAGE_LIST_DETAIL;
//                mesg.obj = jsonMap;
//                handler.sendMessage(mesg);
                Intent agreementIntent = new Intent(context, WebUtilsActivity.class);
                agreementIntent.putExtra("functionTitle","零工推荐");
                agreementIntent.putExtra("contentUrl", GetDataConfing.BASEURL_H5);
                agreementIntent.putExtra("type","3");
                mContext.startActivity(agreementIntent);
            }
        });

        return convertView;
    }

    private void showDate(ViewHolder viewHolder, int position) {
        final JsonMap<String, Object> jsonMap = list.get(position);
        String timeStr = jsonMap.getString("createTime");
        String prevTimeStr;
        if (position - 1 > 0) {
            final JsonMap<String, Object> prevJsonMap = list.get(position - 1);
            prevTimeStr = prevJsonMap.getString("createTime");
        } else {
            prevTimeStr = String.valueOf(System.currentTimeMillis());
        }
        if (TextUtils.isEmpty(timeStr) || TextUtils.equals(timeStr, "null") || TextUtils.isEmpty(prevTimeStr) || TextUtils.equals(prevTimeStr, "null")) {
            viewHolder.rl_day_bg.setVisibility(View.GONE);
        } else {
            int day = Math.abs(DateUtils.getDayDiff(prevTimeStr, timeStr));
            if (day >= 0 && day < 1) {
                viewHolder.rl_day_bg.setVisibility(View.GONE);
            } else {
                viewHolder.rl_day_bg.setVisibility(View.VISIBLE);
                viewHolder.tv_news_time.setText(DateUtils.dateFormat("MM月dd日", timeStr));
            }
        }
    }

    static class ViewHolder {
        @Bind(R.id.rl_day_bg)
        RelativeLayout rl_day_bg;
        @Bind(R.id.tv_news_time)
        TextView tv_news_time;
        @Bind(R.id.rl_news_info)
        RelativeLayout rl_news_info;
        @Bind(R.id.tv_system_news_title)
        TextView tv_system_news_title;
        @Bind(R.id.tv_system_news_content)
        TextView tv_system_news_content;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}