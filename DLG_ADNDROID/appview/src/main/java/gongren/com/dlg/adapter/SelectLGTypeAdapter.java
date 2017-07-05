package gongren.com.dlg.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import gongren.com.dlg.R;
import gongren.com.dlg.javabean.SelectData;

/**
 * 选择零工类型的adapter
 */
public class SelectLGTypeAdapter extends SimpleBaseAdapter<SelectData> {

    private boolean mIsShowDeleteCheckBox;
    ViewHolder holder;
    private Context context;
    private View.OnClickListener mListener;

    public SelectLGTypeAdapter(Context context, List<SelectData> list, View.OnClickListener mListener) {
        super(context, list);
        this.context = context;
        this.mListener = mListener;
    }

    public void setDataBean(List<SelectData> list) {
        this.list = list;
        notifyDataSetChanged();
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
            convertView = inflater.inflate(R.layout.select_lg_type_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SelectData data = (SelectData)list.get(position);
        if (data != null) {
            holder.nameText.setText(data.getDataName());
            if (data.isCheched()) {
                holder.checkbox.setChecked(true);
            } else {
                holder.checkbox.setChecked(false);
            }
        }

        holder.checkbox.setTag(position);
        holder.checkbox.setOnClickListener(mListener);

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
