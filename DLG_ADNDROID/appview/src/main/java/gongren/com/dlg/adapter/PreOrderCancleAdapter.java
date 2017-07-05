package gongren.com.dlg.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import gongren.com.dlg.R;
import gongren.com.dlg.javabean.SelectData;

/**
 * 我的需求，需求详情页 取消的adapter
 */
public class PreOrderCancleAdapter extends SimpleBaseAdapter<SelectData> {

    private ViewHolder holder;
    private int status;
    private boolean mIsShowDeleteCheckBox;
    private View.OnClickListener mListener;

    public PreOrderCancleAdapter(Context context, View.OnClickListener mListener, List<SelectData> list, int status) {
        super(context, list);
        this.status = status;
        this.mListener = mListener;
    }

    public void setDataBean(List<SelectData> list) {
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
            convertView = inflater.inflate(R.layout.pre_cancle_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.checkbox.setVisibility(View.GONE);

        holder.callBtn.setTag("13641634426");//直接传电话号码，以区分，上面的列表.
        holder.callBtn.setOnClickListener(mListener);

        return convertView;
    }

    static class ViewHolder {
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
        @Bind(R.id.cancle_reseason_text)
        TextView cancleReseasonText;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
