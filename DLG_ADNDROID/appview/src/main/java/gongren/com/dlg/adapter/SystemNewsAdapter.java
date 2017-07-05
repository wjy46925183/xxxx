package gongren.com.dlg.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import aym.util.json.JsonMap;
import butterknife.Bind;
import butterknife.ButterKnife;
import gongren.com.dlg.R;
import gongren.com.dlg.activity.NewsInfoActivity;
import com.common.utils.DateUtils;
import com.common.utils.StringUtils;

/**
 * 适配系统消息适配器
 * liukui 2014/01/24
 */
public class SystemNewsAdapter extends SimpleBaseAdapter<JsonMap<String, Object>> {
    private  Handler handler;
    private Context mContext ;

    public SystemNewsAdapter(Context context, List<JsonMap<String, Object>> list, Handler handler) {
        super(context, list);
        mContext = context ;
        this.handler = handler ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list_systen_news, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();
        final JsonMap<String, Object> jsonMap = list.get(position);
        showDate(viewHolder, position);
        viewHolder.tv_system_news_title.setText(StringUtils.verifyString(jsonMap.getString("content")));
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NewsInfoActivity.class);
                intent.putExtra("title",jsonMap.getStringNoNull("title"));
                intent.putExtra("content",jsonMap.getStringNoNull("content"));
                intent.putExtra("createTime",jsonMap.getDouble("createTime"));
                mContext.startActivity(intent);
            }
        });
//        viewHolder.tv_news_time.setText(DateUtils.getCurrentTime((long) jsonMap.getDouble("createTime")));
        return convertView;
    }

    private void showDate(ViewHolder viewHolder, int position) {
        final JsonMap<String, Object> jsonMap = list.get(position);
        String timeStr = jsonMap.getString("createTime");
        viewHolder.tv_news_time.setText(DateUtils.dateFormat("MM月dd日", timeStr));
    }

    static class ViewHolder {
        @Bind(R.id.rl_day_bg)
        View rl_day_bg;
        @Bind(R.id.tv_news_time)
        TextView tv_news_time;
        @Bind(R.id.rl_news_info)
        View rl_news_info;
        @Bind(R.id.tv_system_news_title)
        TextView tv_system_news_title;
        @Bind(R.id.tv_system_news_content)
        TextView tv_system_news_content;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
