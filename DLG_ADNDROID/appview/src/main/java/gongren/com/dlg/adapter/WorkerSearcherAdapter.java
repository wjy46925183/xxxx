package gongren.com.dlg.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;


import butterknife.Bind;
import butterknife.ButterKnife;
import gongren.com.dlg.R;
import gongren.com.dlg.activity.DoingOrderData;
import gongren.com.dlg.utils.IntegerUtils;

/**
 * 公共查询适配器
 * 2017/04/05
 * 刘奎
 */
public class WorkerSearcherAdapter extends BaseAdapter {

    private Context context;
    private int searchTitle, searchItem;
    private List<DoingOrderData> historyList ,hotList;
    private Handler handler;
    private LayoutInflater inflater;
    private final int typeOne= 0,typeTwo = 1;

    public WorkerSearcherAdapter(Context context, int searchTitle, int searchItem, List<DoingOrderData> historyList , List<DoingOrderData> hotList, Handler handler) {
        this.context = context;
        this.searchTitle = searchTitle;
        this.searchItem = searchItem;
        this.historyList = historyList;
        this.hotList = hotList;
        this.handler = handler;
        inflater = LayoutInflater.from(context);
    }

    public void setData( List<DoingOrderData> historyList , List<DoingOrderData> hotList){
        this.historyList = historyList;
        this.hotList = hotList;
        notifyDataSetChanged();
    }
    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        return historyList.size()>position?typeTwo:position==historyList.size()?typeOne:typeTwo;
    }

    @Override
    public int getCount() {
        return historyList.size()+hotList.size()+1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        ViewHolderTitle titleHolder;

        switch (getItemViewType(position)){
            case typeOne:
                if (convertView == null) {
                    convertView = inflater.inflate(searchTitle, null);
                    titleHolder = new ViewHolderTitle(convertView);
                    convertView.setTag(titleHolder);
                } else {
                    titleHolder = (ViewHolderTitle) convertView.getTag();
                }

                titleHolder.titleText.setText("热门零工");
                titleHolder.clearText.setVisibility(View.GONE);
                break;

            case typeTwo:

                if (convertView == null) {
                    convertView = inflater.inflate(searchItem, null);
                    holder = new ViewHolder(convertView);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                if(position<=historyList.size()){

                    final DoingOrderData data = historyList.get(position);
                    if(data.getPostName()==null||data.getPostName().trim().equals("")){




                        holder.itemText.setVisibility(View.INVISIBLE);
                        return convertView;
                    }
                    holder.itemText.setVisibility(View.VISIBLE);

                    holder.itemText.setText(data.getPostName());



                    holder.itemText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Message message = new Message();
                            message.what = IntegerUtils.SELECT_WORKER;
                            message.obj = data;
                            handler.sendMessage(message);
                        }
                    });
                }else{

                    final DoingOrderData data = hotList.get(position-historyList.size()-1);
                    if(data.getPostName()==null||data.getPostName().trim().equals("")) {




                        holder.itemText.setVisibility(View.INVISIBLE);
                        return convertView;
                    }
                    holder.itemText.setVisibility(View.VISIBLE);

                    holder.itemText.setText(data.getPostName());



                    holder.itemText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Message message = new Message();
                            message.what = IntegerUtils.SELECT_WORKER;
                            message.obj = data;
                            handler.sendMessage(message);
                        }
                    });
                }
                break;
        }
        return convertView;
    }

    static class ViewHolderTitle {

        @Bind(R.id.work_search_title)
        TextView titleText;
        @Bind(R.id.work_search_clear)
        TextView clearText;

        ViewHolderTitle(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ViewHolder {

        @Bind(R.id.work_searcher_item)
        TextView itemText;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
