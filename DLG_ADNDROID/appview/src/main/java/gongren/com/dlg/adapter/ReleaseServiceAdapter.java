package gongren.com.dlg.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import gongren.com.dlg.R;

/**
 * Created by Wangjinya on 2017/6/21.
 */

public class ReleaseServiceAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> paths;
    private View.OnClickListener deleteListener;

    public ReleaseServiceAdapter(Context context, List<String> paths, View.OnClickListener deleteListener) {
        this.mContext = context;
        this.paths = paths;
        this.deleteListener = deleteListener;
    }

    @Override
    public int getCount() {
        return paths.size() < 3 ? paths.size() + 1 : paths.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_serice, null);
            holder = new MyViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }

        if (position == paths.size()) {
            holder.iv_photo.setVisibility(View.GONE);
            holder.iv_delete.setVisibility(View.GONE);
            holder.iv_add.setVisibility(View.VISIBLE);
        } else {
            holder.iv_delete.setVisibility(View.VISIBLE);
            holder.iv_photo.setVisibility(View.VISIBLE);
            holder.iv_add.setVisibility(View.GONE);
            String s = paths.get(position);
            Log.i("====s==url adapter", s);
            Glide.with(mContext).load(s).placeholder(R.mipmap.ic_launcher).into(holder.iv_photo);
            holder.iv_delete.setTag(position);
            holder.iv_delete.setOnClickListener(deleteListener);
        }
        return convertView;
    }

    private boolean isDelete = false;

    public void setIsShowDelete(boolean isDelete) {
        this.isDelete = isDelete;
        notifyDataSetChanged();
    }

    class MyViewHolder {
        ImageView iv_photo, iv_delete, iv_add;

        public MyViewHolder(View itemView) {
            iv_delete = (ImageView) itemView.findViewById(R.id.iv_delete);
            iv_photo = (ImageView) itemView.findViewById(R.id.iv_item_serice);
            iv_add = (ImageView) itemView.findViewById(R.id.iv_item_add);
        }
    }
}
