package gongren.com.dlg.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.services.help.Tip;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import gongren.com.dlg.R;

/**
 * 公共查询适配器
 * 2017/04/05
 * 刘奎
 */
public class AddressSearcherAdapter extends SimpleBaseAdapter<Tip> {
    ViewHolder holder;
    private Context context;

    public AddressSearcherAdapter(Context context, List<Tip> list) {
        super(context, list);
        this.context = context;
    }

    public void setDataBean(List<Tip> list) {
        this.list = list;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.search_address_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//        DoingOrderData dataBean = list.get(position);
        holder.nameText.setText(list.get(position).getName());
        holder.addressText.setText(list.get(position).getAddress());
        return convertView;
    }

    static class ViewHolder {

        @Bind(R.id.searcher_address_name)
        TextView nameText;
        @Bind(R.id.searcher_address_address)
        TextView addressText;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
