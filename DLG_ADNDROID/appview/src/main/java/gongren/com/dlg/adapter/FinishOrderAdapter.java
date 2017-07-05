package gongren.com.dlg.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import gongren.com.dlg.R;
import gongren.com.dlg.activity.PingFenActivity;
import gongren.com.dlg.javabean.master.masterlist.ListBean;

/**
 * 我的需求，需求详情页 已完成的adapter
 */
public class FinishOrderAdapter extends SimpleBaseAdapter<ListBean> {
    private Context context;
    private View.OnClickListener mListener;
    private int status = -1; //50 已取消

    public FinishOrderAdapter(Context context,
                              List<ListBean> list, View.OnClickListener mListener) {
        super(context, list);
        this.context = context;
        this.mListener = mListener;
    }

    public FinishOrderAdapter(Context context,
                              List<ListBean> list,int status, View.OnClickListener mListener) {
        super(context, list);
        this.context = context;
        this.status = status;
        this.mListener = mListener;
    }

    public void setDataBean(List<ListBean> list) {
        this.list = list;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.finish_order_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ListBean listBean = list.get(position);
        holder.fenText.setText(listBean.creditCount == null ? 36.5f + "" : Float.parseFloat(listBean.creditCount) + "");
        holder.starbar01.setRating(listBean.scoreCount == null ? 5.0f : Float.parseFloat(listBean.scoreCount));//星星数量
        String logo = listBean.logo;
        Glide.with(context).load(logo).into(holder.ivHead);
        holder.nameText.setText(listBean.name);
        holder.checkbox.setVisibility(View.GONE);
        float tipAmount = 0;
        if(!TextUtils.isEmpty(listBean.tipAmount)){
            tipAmount = Float.parseFloat(listBean.tipAmount);
        }

        float price = listBean.amount == null?0.0f:Float.parseFloat(listBean.amount) + tipAmount;
        if(tipAmount>0){
            holder.incomeText.setText(price+"元(含小费"+tipAmount+"元)");
        }else {
            holder.incomeText.setText(price+"元");
        }

        holder.callBtn.setTag(listBean.phone);
        holder.callBtn.setOnClickListener(mListener);

        holder.pingfenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PingFenActivity.class);
                intent.putExtra("isGuYuan", false);
                intent.putExtra("item", list.get(position));
                ((Activity)context).startActivityForResult(intent,200);
            }
        });

        if ("0".equals(list.get(position).isEvaluate)) {//没有评价
            holder.starbar.setVisibility(View.GONE);
            holder.pingfenBtn.setVisibility(View.VISIBLE);
            holder.pingfenHint.setVisibility(View.VISIBLE);
            holder.pingfenHint.setText("未评价");
        } else {//已评价过
            holder.pingfenBtn.setVisibility(View.GONE);
            holder.starbar.setVisibility(View.VISIBLE);
            holder.pingfenHint.setVisibility(View.VISIBLE);
            holder.pingfenHint.setText("已评价");
            holder.starbar.setRating(listBean.scoreCount == null ? 5.0f : Float.parseFloat(listBean.scoreCount));
        }
        if(this.status == 50){
            holder.layoutPingfen.setVisibility(View.GONE);
            holder.line2.setVisibility(View.GONE);
            holder.incomeTab.setText("已取消");
            holder.incomeText.setText(listBean.cancelCause);
            holder.incomeText.setTextColor(context.getResources().getColor(R.color.right_change_textcolor));
        }
        return convertView;
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
        @Bind(R.id.income_tab)
        TextView incomeTab;
        @Bind(R.id.income_text)
        TextView incomeText;
        @Bind(R.id.line2)
        View line2;
        @Bind(R.id.layout_pingfen)
        RelativeLayout layoutPingfen;
        @Bind(R.id.pingfen_btn)
        TextView pingfenBtn;
        @Bind(R.id.pingfen_hint)
        TextView pingfenHint;
        @Bind(R.id.starbar)
        RatingBar starbar;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
