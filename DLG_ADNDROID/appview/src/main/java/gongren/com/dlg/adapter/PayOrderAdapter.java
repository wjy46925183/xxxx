package gongren.com.dlg.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import gongren.com.dlg.R;
import gongren.com.dlg.activity.MasterDetailsActivity;
import gongren.com.dlg.javabean.master.masterlist.ListBean;
import gongren.com.dlg.javabean.master.masterlist.OrderStatusListBean;
import gongren.com.dlg.utils.ToastUtils;

/**
 * 我的需求，需求详情页 正在进行的adapter
 */
public class PayOrderAdapter extends BaseAdapter {
    private Context context;
    private OrderStatusListBean mStatusListBean;
    private List<Boolean> mBooleanList;
    private int type = 0;
    private CheckBox mCheckBox;
    private Map<Integer, Float> tips;
    private boolean input = true;
    private View.OnClickListener listener;

    public PayOrderAdapter(Context context,
                           OrderStatusListBean bean,
                           CheckBox checkBox, PayMoneyCallBack moneyCallBack, View.OnClickListener listener) {
        this.context = context;
        this.mStatusListBean = bean;
        mBooleanList = new ArrayList<>();
        mCheckBox = checkBox;
        tips = new HashMap<>();
        this.listener = listener;
        mPayMoneyCallBack = moneyCallBack;
        if (mStatusListBean == null || mStatusListBean.list == null)return;
        for (int i = 0; i < mStatusListBean.list.size(); i++) {
            mBooleanList.add(false);
            tips.put(i, 0f);
        }
    }

    @Override
    public int getCount() {
        if(mStatusListBean == null || mStatusListBean.list == null || mStatusListBean.list.size()<0){
            return 0;
        }else {
            return mStatusListBean.list.size();
        }
    }

    @Override
    public ListBean getItem(int position) {
        return mStatusListBean.list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.pay_order_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ListBean item = getItem(position);
        final double totalPrice = item.totalPrice;
        final double insuranceAmount = TextUtils.isEmpty(item.insuranceAmount) ? 0 :  Double.parseDouble(item.insuranceAmount);
        if (type == 1) {//全选
            holder.checkbox.setChecked(true);
            for (int i = 0; i < mBooleanList.size(); i++) {
                mBooleanList.set(i, true);
            }
            if (mPayMoneyCallBack != null) {
                mPayMoneyCallBack.pay(totalPrice * mBooleanList.size());
                mPayMoneyCallBack.bao(insuranceAmount*mBooleanList.size());
            }
        } else if (type == 2) {//全部都不选
            holder.checkbox.setChecked(false);
            for (int i = 0; i < mBooleanList.size(); i++) {
                mBooleanList.set(i, false);
            }
            if (mPayMoneyCallBack != null) {
                mPayMoneyCallBack.pay(0);//全都不选的时候为0
                mPayMoneyCallBack.bao(0);
                mPayMoneyCallBack.tip(0,"");
                mPayMoneyCallBack.total(0);
            }
        } else {
            holder.checkbox.setChecked(mBooleanList.get(position));
        }
        Glide.with(context).load(item.logo).into(holder.ivHead);
        if(TextUtils.isEmpty(item.name)){
            holder.nameText.setText(item.phone);
        }else {
            holder.nameText.setText(item.name);
        }
        if(insuranceAmount==0){
            holder.bao_linear.setVisibility(View.GONE);
        }

        holder.fenText.setText(item.creditCount==null?36.5f + "":Float.parseFloat(item.creditCount)+"");
        holder.starbar01.setRating(item.scoreCount == null ? 5.0f : Float.parseFloat(item.scoreCount));
        holder.incomeText.setText(item.totalPrice+"元");
        holder.baoxian.setText(insuranceAmount+"元");
        holder.callBtn.setTag(item.phone);
        holder.callBtn.setOnClickListener(listener);
        Glide.with(context).load(item.logo).fitCenter()
                .override(150, 150)
                .error(R.mipmap.morentouxiang).into(holder.ivHead);
        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mBooleanList.set(position, buttonView.isChecked());
                boolean is_all = true;
                int num = 0;
                float total = 0;
                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < mBooleanList.size(); i++) {
                    if (!mBooleanList.get(i)) {
                        mCheckBox.setChecked(false);
                        is_all = false;
                    } else {
                        num++;
                        total+= tips.get(i);
                        stringBuffer.append(mStatusListBean.list.get(i).businessNumber+"#"+tips.get(i)+",");
                    }
                }
                if (mPayMoneyCallBack != null) {
                    mPayMoneyCallBack.pay(totalPrice * num);
                    mPayMoneyCallBack.tip(total,stringBuffer.toString());
                    mPayMoneyCallBack.bao(insuranceAmount*num);
                    mPayMoneyCallBack.total(totalPrice*num+total+insuranceAmount);
                }
                if (is_all) {
                    mCheckBox.setChecked(true);
                }
            }
        });
        holder.xiaofeiEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                float xiaofei = 0;
                if(!TextUtils.isEmpty(s.toString())){
                    xiaofei = Float.parseFloat(s.toString().trim());
                }
                float i1 = xiaofei;
//                if(xiaofei > item.totalPrice){
//                    i1 = 0 ;
//                }
                if(xiaofei > item.totalPrice){
                    ToastUtils.showToastShort(context,"小费金额不能大于工资");
                    i1 = item.totalPrice;
                    if(input){
                        input = false;
                        holder.xiaofeiEdit.setText(""+item.totalPrice);
                        holder.xiaofeiEdit.setSelection(holder.xiaofeiEdit.getText().length());
                    }
                }else {
                    if(input){
                        input = false;
                        holder.xiaofeiEdit.setSelection(holder.xiaofeiEdit.getText().length());
                    }
                }
                tips.put(position, i1);
                int num = 0;
                float total = 0;
                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < mBooleanList.size(); i++) {
                    if (mBooleanList.get(i)) {
                        num++;
                        total+= tips.get(i);
                        stringBuffer.append(mStatusListBean.list.get(i).businessNumber+"#"+tips.get(i)+",");
                    }
                }
                if (mPayMoneyCallBack != null) {
                    mPayMoneyCallBack.pay(totalPrice * num);
                    mPayMoneyCallBack.bao(insuranceAmount);
                    mPayMoneyCallBack.tip(total,stringBuffer.toString());
                    mPayMoneyCallBack.total(totalPrice*num+total+insuranceAmount);
                }
                input = true ;
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MasterDetailsActivity.class);
                intent.putExtra("businessNumber", item.businessNumber);
                if(!TextUtils.isEmpty(holder.xiaofeiEdit.getText().toString().trim())){
                    intent.putExtra("orders", item.businessNumber+"#"+holder.xiaofeiEdit.getText().toString());
                }else {
                    intent.putExtra("orders", item.businessNumber+"#"+0);
                }
                //订单业务编号
                intent.putExtra("isfrom", 1);
                String textName = holder.xiaofeiEdit.getText().toString().trim();
                double tip = (TextUtils.isEmpty(textName) ? 0 : Double.parseDouble(textName));
                intent.putExtra("tip",tip);
                intent.putExtra("pay",totalPrice);
                intent.putExtra("bao",insuranceAmount);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    private PayMoneyCallBack mPayMoneyCallBack;

    //全选
    public void setAllCheck(int type, CheckBox checkBox) {
        this.type = type;
        mCheckBox = checkBox;
        notifyDataSetChanged();
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
        @Bind(R.id.income_tab)
        TextView incomeTab;
        @Bind(R.id.income_text)
        TextView incomeText;
        @Bind(R.id.line2)
        View line2;
        @Bind(R.id.xiaofei_tab)
        TextView xiaofeiTab;
        @Bind(R.id.xiaofei_edit)
        EditText xiaofeiEdit;
        @Bind(R.id.util_text)
        TextView utilText;
        @Bind(R.id.item_content_linear)
        LinearLayout itemContentLinear;
        @Bind(R.id.baoxian_tab)
        TextView baoxian;
        @Bind(R.id.baoxian_linear)
        RelativeLayout bao_linear;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface PayMoneyCallBack {
        void pay(double pay);

        void tip(double tip,String orders);

        void total(double totalMoney);

        void bao(double bao);
    }
}
