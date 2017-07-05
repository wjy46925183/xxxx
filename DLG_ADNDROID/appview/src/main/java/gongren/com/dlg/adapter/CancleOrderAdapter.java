package gongren.com.dlg.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import aym.util.json.JsonMap;
import butterknife.Bind;
import butterknife.ButterKnife;
import gongren.com.dlg.R;

/**
 * 我的需求，零工列表，取消订单的adapter
 */
public class CancleOrderAdapter extends SimpleBaseAdapter<JsonMap<String, Object>> {

    private boolean mIsShowDeleteCheckBox;
    ViewHolder holder;
    private Context context;
    private View.OnClickListener mListener;

    public CancleOrderAdapter(Context context, List<JsonMap<String, Object>> list, View.OnClickListener mListener) {
        super(context, list);
        this.context = context;
        this.mListener = mListener;
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
            convertView = inflater.inflate(R.layout.cancle_order_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.checkbox.setTag(position);
        holder.checkbox.setOnClickListener(mListener);

        final JsonMap<String, Object> itemData = list.get(position);
        if (itemData != null) {
            holder.nameText.setText(itemData.getStringNoNull("dataName"));
            if (itemData.getBoolean("isChecked",false)) {
                holder.checkbox.setChecked(true);
            } else {
                holder.checkbox.setChecked(false);
            }
        }

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.name_text)
        TextView nameText;
        @Bind(R.id.checkbox)
        CheckBox checkbox;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
