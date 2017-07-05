package gongren.com.dlg.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import gongren.com.dlg.R;
import gongren.com.dlg.javabean.master.masterlist.ListBean;
import gongren.com.dlg.javabean.master.masterlist.OrderStatusListBean;

/**
 * 我的需求，需求详情页adapter
 */
public class PreOrderAdapter extends BaseAdapter {

    private LayoutInflater inflate;
    private OrderStatusListBean bean;
    private Context mContext;
    private int type = 0;
    private List<Boolean> mBooleanList;
    private CheckBox mCheckBox;
    private View.OnClickListener listener;

    public PreOrderAdapter(Context context, OrderStatusListBean bean, View.OnClickListener listener) {
        inflate = LayoutInflater.from(context);
        this.bean = bean;
        this.mContext = context;
        mBooleanList = new ArrayList<>();
        this.listener = listener;
        for (int i = 0; i < bean.list.size(); i++) {
            mBooleanList.add(i, false);
        }
    }

    public void setDataBean(OrderStatusListBean bean) {
        this.bean = bean;
    }

    @Override
    public int getCount() {
        if (bean == null || bean.list == null) {
            return 0;
        } else {
            int size = bean.list.size();
            return size;
        }
    }

    @Override
    public ListBean getItem(int position) {
        return bean.list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public List<Boolean> getBool() {
        return mBooleanList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflate.inflate(R.layout.order_people_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ListBean item = getItem(position);
        if (type == 1) {//全选
            holder.checkbox.setChecked(true);
            for (int i = 0; i < mBooleanList.size(); i++) {
                mBooleanList.set(i, true);
            }
        } else if (type == 2) {//全部都不选
            holder.checkbox.setChecked(false);
            for (int i = 0; i < mBooleanList.size(); i++) {
                mBooleanList.set(i, false);
            }
        } else {
            holder.checkbox.setChecked(mBooleanList.get(position));
        }
        holder.checkbox.setVisibility(View.VISIBLE);
        holder.layouyHint.setVisibility(View.GONE);
        if (bean.status == 6 || bean.status == 7) {
            holder.layouyHint.setVisibility(View.VISIBLE);
            holder.checkbox.setVisibility(View.GONE);
            if (bean.status == 6) {
                holder.tvHint.setText("等待雇员同意");
            } else {
                holder.tvHint.setText("拒绝邀请");
            }
            holder.tvKnow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((Activity) mContext).finish();
                }
            });
        } else {
            holder.layouyHint.setVisibility(View.GONE);
        }

        holder.nameText.setText(item.name);
        holder.fenText.setText(item.creditCount == null ? 36.5f + "" : Float.parseFloat(item.creditCount) + "");
        holder.starbar01.setRating(item.scoreCount == null ? 5.0f : Float.parseFloat(item.scoreCount));

        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mBooleanList.set(position, buttonView.isChecked());
                boolean is_all = true;
                for (int i = 0; i < mBooleanList.size(); i++) {
                    if (!mBooleanList.get(i)) {
                        mCheckBox.setChecked(false);
                        is_all = false;
                    }
                }
                if (is_all) {
                    mCheckBox.setChecked(true);
                }
              /*  if(getCheckedList!=null){
                    getCheckedList.onGetChecked(mBooleanList);
                }*/
            }
        });
        Glide.with(mContext).load(item.logo).into(holder.ivHead);
        holder.callBtn.setTag(getItem(position).phone);
        holder.callBtn.setOnClickListener(listener);

        //点击条目进行跳转
        return convertView;
    }

    //全选
    public void setAllCheck(int type, CheckBox checkBox) {
        this.type = type;
        mCheckBox = checkBox;
        notifyDataSetChanged();
    }

    class ViewHolder {
        @Bind(R.id.checkbox)
        CheckBox checkbox;
        @Bind(R.id.iv_head)
        CircleImageView ivHead;
        @Bind(R.id.name_text)
        TextView nameText;
        @Bind(R.id.fen_text)
        TextView fenText;
        @Bind(R.id.starbar_01)
        RatingBar starbar01;
        @Bind(R.id.call_btn)
        ImageView callBtn;
        @Bind(R.id.people_line)
        View peopleLine;
        @Bind(R.id.tv_know)
        TextView tvKnow;
        @Bind(R.id.tv_hint)
        TextView tvHint;
        @Bind(R.id.layout_hint)
        RelativeLayout layouyHint;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    //public GetCheckedList getCheckedList;
    public List<Boolean> getCheckedtList() {
        return mBooleanList;
    }
    /* public interface GetCheckedList{
       void   onGetChecked(List<Boolean> mBooleanList);
    }*/
}
