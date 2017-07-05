package gongren.com.dlg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * 简单继承BaseAdapter的Adapter都可继承SimpleBaseAdapter
 */
public abstract class SimpleBaseAdapter<T> extends BaseAdapter {
    protected Context context;
    protected LayoutInflater inflater;
    protected List<T> list;

    public SimpleBaseAdapter(Context context, List<T> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    /**
     * 刷新数据方法
     *
     * @param datas
     */
    public void refreshDatas(List<T> datas, int currentPage) {
        if (currentPage == 0) {
            list.clear();
        }
        list.addAll(datas);
        this.notifyDataSetChanged();
    }

    public void refreshDatasPage(List<T> datas, int currentPage) {
        if (currentPage == 1) {
            list.clear();
        }
        list.addAll(datas);
        this.notifyDataSetChanged();
    }

    /**
     * 刷新数据方法
     *
     * @param datas
     */
    public void refreshDatas(List<T> datas) {
        list.addAll(datas);
        this.notifyDataSetChanged();
    }
    public void setDatas(List<T> datas){
        this.list = datas;
        notifyDataSetChanged();
    }

    public void clearNotify(){
        list.clear();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list == null ? null : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);
}
