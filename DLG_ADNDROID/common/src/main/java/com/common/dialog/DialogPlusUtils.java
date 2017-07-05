package com.common.dialog;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.common.string.StringUtil;
import com.common.view.dialogplus.DialogPlus;
import com.common.view.dialogplus.Holder;
import com.common.view.dialogplus.OnBackPressListener;
import com.common.view.dialogplus.OnCancelListener;
import com.common.view.dialogplus.OnClickListener;
import com.common.view.dialogplus.OnDismissListener;
import com.common.view.dialogplus.ViewHolder;

import demo.java.com.common.R;


/**
 * 作者：wangdakuan
 * 主要功能：对话框
 * 创建时间：16/8/4 13:03
 */
public class DialogPlusUtils {

    public static final int LIST = 1001;  //列表 listView
    public static final int GRID = 1002;  //网格 gridView
    public static final int VIEW = 1003;  //任何布局

    /**
     * Gravity.BOTTOM
     * Gravity.CENTER
     * Gravity.TOP
     */
    private int gravity = Gravity.CENTER;

    private boolean isHeader = true; //是否显示默认头部
    private boolean isFooter = true; //是否显示默认底部
    private boolean isExpanded = false; // 是否可以拉伸
    //    private int holder = VIEW;
    private Holder holderView; //布局
    private RecyclerView.Adapter adapter; //适配器

    private OnCancelListener cancelListener; //取消
    private OnDismissListener dismissListener; //销毁
    private OnClickListener clickListener; //
    private OnConfirmListener onConfirmListener; //确认事件
    private OnBackPressListener backPressListener;
    private OnCloseListener onCloseListener; //取消事件

    private int contentBackgroundResource = R.drawable.bg_dialog_white; //R.drawable.bg_dialog_white

    private Context context;
    private String titleName; //标题
    private String confirmName; //确定按钮提示文字
    private String closeName; //取消按钮提示文字
    private boolean isCancelable = true;
    private String basisName; //默认内容
    private int gravityText = Gravity.TOP | Gravity.LEFT;
    private DialogPlus dialog;

    public interface OnConfirmListener {   //点击确定的回调
        void confirmListener(DialogPlus dialog, View view);
    }

    public interface OnCloseListener {   //点击确定的回调
        void closeListener(DialogPlus dialog, View view);
    }

    public DialogPlusUtils() {
    }

    public DialogPlusUtils(Context context) {
        this.context = context;
    }

    public static DialogPlusUtils Builder(Context context) {
        return new DialogPlusUtils(context);
    }

    public DialogPlusUtils basisDialogPlus(String hint) {
        this.basisName = hint;
        return this;
    }

    public DialogPlusUtils basisDialogPlus(Integer hint) {
        this.basisName = context.getResources().getString(hint);
        return this;
    }

    /**
     * 设置从哪个位置弹出
     *
     * @param gravity
     * @return
     */
    public DialogPlusUtils setGravity(int gravity) {
        this.gravity = gravity;
        return this;
    }

    public DialogPlusUtils setCancelable(boolean cancelable) {
        isCancelable = cancelable;
        return this;
    }

    /**
     * 确认按钮字体
     * @param confirmName
     * @return
     */
    public DialogPlusUtils setConfirmName(Integer confirmName) {
        return setConfirmName(context.getResources().getString(confirmName));
    }
    /**
     * 确认按钮字体
     * @param confirmName
     * @return
     */
    public DialogPlusUtils setConfirmName(String confirmName) {
        this.confirmName = confirmName;
        return this;
    }

    /**
     * 取消按钮字体
     * @param closeName
     * @return
     */
    public DialogPlusUtils setCloseName(Integer closeName) {
        return setConfirmName(context.getResources().getString(closeName));
    }


    /**
     * 取消按钮字体
     * @param closeName
     * @return
     */
    public DialogPlusUtils setCloseName(String closeName) {
        this.closeName = closeName;
        return this;
    }

    /**
     * 设置默认内容字体位置
     * @param gravityText
     * @return
     */
    public DialogPlusUtils setGravityText(int gravityText) {
        this.gravityText = gravityText;
        return this;
    }

    /**
     * 设置是否显示头部标题
     *
     * @param isHeader
     * @return
     */
    public DialogPlusUtils setIsHeader(boolean isHeader) {
        this.isHeader = isHeader;
        return this;
    }

    /**
     * 设置是否显示底部
     *
     * @param isFooter
     * @return
     */
    public DialogPlusUtils setIsFooter(boolean isFooter) {
        this.isFooter = isFooter;
        return this;
    }

    /**
     * Dialog 背景框
     * @param contentBackgroundResource
     * @return
     */
    public DialogPlusUtils setContentBackgroundResource(int contentBackgroundResource) {
        this.contentBackgroundResource = contentBackgroundResource;
        return this;
    }

    /**
     * 设置是否可拉伸
     *
     * @param isExpanded
     * @return
     */
    public DialogPlusUtils setIsExpanded(boolean isExpanded) {
        this.isExpanded = isExpanded;
        return this;
    }

    /**
     * @param holder     列表还是布局(LIST GRID VIEW)
     * @param holderView new ViewHolder(view)(里面可以传一个view也可以传一个layout) or (如果传的是LIST或GRID)new ListHolder() or new GridHolder(3)(参数代表几列)
     * @return
     */
    public DialogPlusUtils setHolder(int holder, Holder holderView) {
//        this.holder = holder;
        this.holderView = holderView;
        return this;
    }

    /**
     * @param holderView new ViewHolder(view)(里面可以传一个view也可以传一个layout) or (如果传的是LIST或GRID)new ListHolder() or new GridHolder(3)(参数代表几列)
     * @return
     */
    public DialogPlusUtils setHolder(Holder holderView) {
        this.holderView = holderView;
        return this;
    }


    /**
     * 如果不是VIEW 必须要设置适配器
     *
     * @param adapter 适配器
     * @return
     */
    public DialogPlusUtils setAdapter(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
        return this;
    }

    public DialogPlusUtils setOnCancelListener(OnCancelListener cancelListener) {
        this.cancelListener = cancelListener;
        return this;
    }

    public DialogPlusUtils setOnDismissListener(OnDismissListener dismissListener) {
        this.dismissListener = dismissListener;
        return this;
    }

    public DialogPlusUtils setOnClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
        return this;
    }

    public DialogPlusUtils setOnConfirmListener(OnConfirmListener onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
        return this;
    }

    public DialogPlusUtils setOnCloseListener(OnCloseListener onCloseListener) {
        this.onCloseListener = onCloseListener;
        return this;
    }

    public DialogPlusUtils setBackPressListener(OnBackPressListener backPressListener) {
        this.backPressListener = backPressListener;
        return this;
    }

    /**
     * 设置标题
     *
     * @param titleName 标题名称
     * @return
     */
    public DialogPlusUtils setTitleName(String titleName) {
        this.titleName = titleName;
        return this;
    }

    public void initClickListener() {
        if (null == clickListener) {
            clickListener = new OnClickListener() {
                @Override
                public void onClick(DialogPlus dialog, View view) {
                    if (view.getId() == R.id.footer_close_button) {   //点击取消按钮
                        if (null != onCloseListener) {
                            onCloseListener.closeListener(dialog, view);
                        }
                        dialog.dismiss();
                    }
                    if (view.getId() == R.id.footer_confirm_button) {  //点击确定按钮 回调到acitivity页面
                        if (null != onConfirmListener) {
                            onConfirmListener.confirmListener(dialog, view);
                        }
                    }
                    if (view.getId() == R.id.btn_close) {  //点击确定按钮 回调到acitivity页面
                        if (null != onCloseListener) {
                            onCloseListener.closeListener(dialog, view);
                        }
                        dialog.dismiss();
                    }
                }
            };
        }
        if (null == cancelListener) {
            cancelListener = new OnCancelListener() {
                @Override
                public void onCancel(DialogPlus dialog) {

                }
            };
        }
        if (null == dismissListener) {
            dismissListener = new OnDismissListener() {
                @Override
                public void onDismiss(DialogPlus dialog) {

                }
            };
        }
    }

    /**
     * 创建
     *
     * @return
     */
    public DialogPlus showCompleteDialog() {
        initClickListener();
        View header = null;
        View footer = null;

        LayoutInflater inflater = LayoutInflater.from(context);

        if (isHeader) {
            header = inflater.inflate(R.layout.dialog_header, null);
            TextView textView = (TextView) header.findViewById(R.id.text_title);
            textView.setText(titleName);
//            if (isFooter) {
//                header.findViewById(R.id.btn_close).setVisibility(View.GONE);
//            }
        }
        if (isFooter) {
            footer = inflater.inflate(R.layout.dialog_footer, null);
            if (!StringUtil.isEmpty(confirmName)) {
                ((TextView) footer.findViewById(R.id.footer_confirm_button)).setText(confirmName);
            }
            if (!StringUtil.isEmpty(closeName)) {
                ((TextView) footer.findViewById(R.id.footer_close_button)).setText(closeName);
            }
        }
        if (null == adapter) {
            adapter = new RecyclerView.Adapter() {
                @Override
                public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    return null;
                }

                @Override
                public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

                }

                @Override
                public int getItemCount() {
                    return 0;
                }
            };
        }
        if (null == holderView) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_dialog_basis, null);
            TextView textView = (TextView) view.findViewById(R.id.text);
            textView.setText(basisName);
            textView.setGravity(gravityText);
            holderView = new ViewHolder(view);
        }
        if(isCancelable && null == backPressListener){
            backPressListener = new OnBackPressListener() {
                @Override
                public void onBackPressed(DialogPlus dialogPlus) {
                    dialogPlus.dismiss();
                }
            };
        }

        dialog = DialogPlus.newDialog(context)
                .setContentHolder(holderView)
                .setHeader(header)
                .setFooter(footer)
                .setCancelable(isCancelable)
                .setGravity(gravity)
                .setAdapter(adapter)
                .setOnClickListener(clickListener)
                .setOnDismissListener(dismissListener)
                .setOnCancelListener(cancelListener)
                .setExpanded(isExpanded)
                .setContentBackgroundResource(contentBackgroundResource)
//        .setContentWidth(800)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setOnBackPressListener(backPressListener)
//                .setOverlayBackgroundResource(R.drawable.bg_dialog_white)
//        .setContentBackgroundResource(R.drawable.corner_background)
                //                .setOutMostMargin(0, 100, 0, 0)
                .create();
        dialog.show();
        return dialog;
    }
}
