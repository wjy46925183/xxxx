package gongren.com.dlg.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import aym.util.json.JsonMap;
import butterknife.Bind;
import butterknife.ButterKnife;
import gongren.com.dlg.R;

/**
 * 评价页的评价文字选择。
 */
public class PJWordsAdapter extends SimpleBaseAdapter<JsonMap<String, Object>> {

    private boolean mIsShowDeleteCheckBox;
    ViewHolder holder;
    private Context context;

    public PJWordsAdapter(Context context, List<JsonMap<String, Object>> list) {
        super(context, list);
        this.context = context;
    }

    public void setDataBean(List<JsonMap<String, Object>> list) {
        this.list = list;
    }

    public void showDeleteCheckBox() {
        mIsShowDeleteCheckBox = true;
        notifyDataSetChanged();
    }

    public void hideDeleteCheckBox() {
        mIsShowDeleteCheckBox = false;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.pj_words_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final JsonMap<String, Object> data = list.get(position);

        if (data != null) {
            holder.btn.setText(data.getStringNoNull("dataValue"));
            if (data.getBoolean("isChecked",false)) {
                holder.btn.setTextColor(context.getResources().getColor(R.color.yellow_textcolor));
                holder.btn.setBackgroundResource(R.drawable.gw_items_bg);
            } else {
                holder.btn.setTextColor(context.getResources().getColor(R.color.gw_unselect_textcolor));
                holder.btn.setBackgroundResource(R.drawable.gw_item_bg);
            }
        }
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.btn)
        TextView btn;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
