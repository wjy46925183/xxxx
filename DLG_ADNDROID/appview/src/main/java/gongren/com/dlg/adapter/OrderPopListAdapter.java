package gongren.com.dlg.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import aym.util.json.JsonMap;
import butterknife.Bind;
import butterknife.ButterKnife;
import gongren.com.dlg.R;

/**
 * Created by Administrator on 2017/3/31.
 */
public class OrderPopListAdapter extends SimpleBaseAdapter<JsonMap<String,Object>> {

    public OrderPopListAdapter(Context context, List<JsonMap<String,Object>> list) {
        super(context, list);
    }

    public void setDatas(List<JsonMap<String,Object>> list){
        super.list=list;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_orderpoplist, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        JsonMap<String, Object> jaonMap =list.get(position);
        if(jaonMap.getBoolean("flag")){
            holder.cb.setChecked(true);
        }else{
            holder.cb.setChecked(false);
        }
        holder.unit.setText(jaonMap.getString("title"));
        holder.num.setText("("+jaonMap.getString("num")+")");
        holder.text.setText(jaonMap.getString("status"));
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.cb)
        CheckBox cb;
        @Bind(R.id.unit)
        TextView unit;
        @Bind(R.id.text)
        TextView text;
        @Bind(R.id.num)
        TextView num;
        @Bind(R.id.layout_click_cb)
        LinearLayout layoutClickCb;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
