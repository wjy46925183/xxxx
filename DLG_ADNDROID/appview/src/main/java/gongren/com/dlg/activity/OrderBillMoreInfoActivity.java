package gongren.com.dlg.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gongren.com.dlg.R;
import gongren.com.dlg.javabean.InvoiceMoreInfoEvent;
import gongren.com.dlg.javabean.InvoiceTemplate;
import com.common.utils.SharedPreferencesUtils;
import gongren.com.dlg.utils.ToastUtils;

public class OrderBillMoreInfoActivity extends BaseActivity {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_save)
    TextView tvSave;

    @Bind(R.id.et_taxpayer_distinguish_number)
    EditText etTaxpayerDistinguishNumber;
    @Bind(R.id.et_company_address)
    EditText etCompanyAddress;
    @Bind(R.id.et_company_phone)
    EditText etCompanyPhone;
    @Bind(R.id.et_bank_account)
    EditText etBankAccount;
    @Bind(R.id.et_bank_account_number)
    EditText etBankAccountNumber;
    @Bind(R.id.et_remarks_explain)
    EditText etRemarksExplain;

    private InvoiceTemplate invoiceTemplate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_bill_more_info);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitle.setText("按订单开票");
        tvSave.setVisibility(View.GONE);

        invoiceTemplate = (InvoiceTemplate) getIntent().getSerializableExtra("InvoiceTemplate");
        if(invoiceTemplate!=null){
            etTaxpayerDistinguishNumber.setText(invoiceTemplate.taxpayerIdentificationCode);
            etCompanyAddress.setText(invoiceTemplate.registerAddress);
            etCompanyPhone.setText(invoiceTemplate.checkTakerPhone);
            etBankAccount.setText(invoiceTemplate.bankName);
            etBankAccountNumber.setText(invoiceTemplate.bankAccount);
        }
        String remarksExplain = SharedPreferencesUtils.getString(this, "remarksExplain");
            etRemarksExplain.setText(remarksExplain);

    }
    @OnClick({R.id.iv_back, R.id.tv_confirm})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.tv_confirm:
                String taxpayerDistinguish = etTaxpayerDistinguishNumber.getText().toString().trim();
                String companyAddress = etCompanyAddress.getText().toString().trim();
                String companyPhone = etCompanyPhone.getText().toString().trim();
                String bankAccountText = etBankAccount.getText().toString().trim();
                String bankAccountNumber = etBankAccountNumber.getText().toString().trim();
                String remarksExplain = etRemarksExplain.getText().toString().trim();
                if(TextUtils.isEmpty(taxpayerDistinguish)){
                    ToastUtils.showToastShort(context, "纳税人识别号不能为空");
                    return;
                }
                if(TextUtils.isEmpty(companyAddress)){
                    ToastUtils.showToastShort(context, "公司地址不能为空");
                    return;
                }
                if(TextUtils.isEmpty(companyPhone)){
                    ToastUtils.showToastShort(context, "公司电话不能为空");
                    return;
                }
                if(TextUtils.isEmpty(bankAccountText)){
                    ToastUtils.showToastShort(context, "开户行不能为空");
                    return;
                }
                if(TextUtils.isEmpty(bankAccountNumber)){
                    ToastUtils.showToastShort(context, "开户行帐号不能为空");
                    return;
                }
                SharedPreferencesUtils.saveString(this,"remarksExplain",remarksExplain);
                EventBus.getDefault().post(new InvoiceMoreInfoEvent(taxpayerDistinguish,
                        companyAddress, companyPhone, bankAccountText, bankAccountNumber, remarksExplain));
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
