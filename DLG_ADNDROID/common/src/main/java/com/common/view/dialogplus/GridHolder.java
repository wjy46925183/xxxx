package com.common.view.dialogplus;

import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import demo.java.com.common.R;

public class GridHolder implements HolderAdapter {

    private final int columnNumber;

    private int backgroundResource;

    private RecyclerView gridView;
    private LinearLayout mFooterView;
    private LinearLayout mHeaderView;
    private GridLayoutManager gridLayoutManager;

    //  private GridView gridView;
    private ViewGroup headerContainer;
    private ViewGroup footerContainer;
    private OnHolderListener listener;
    private View.OnKeyListener keyListener;
    private NestedScrollView scrollView;
    private View headerView;
    private View footerView;
    private View outMostView;

    public GridHolder(int columnNumber) {
        this.columnNumber = columnNumber;
    }

    @Override
    public void addHeader(View view) {
        if (view == null) {
            return;
        }
        headerContainer.addView(view);
        headerView = view;
    }

    @Override
    public void addFooter(View view) {
        if (view == null) {
            return;
        }
        footerContainer.addView(view);
        footerView = view;
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        gridView.setAdapter(adapter);
    }

    @Override
    public void setBackgroundResource(int colorResource) {
        this.backgroundResource = colorResource;
    }

    @Override
    public View getView(LayoutInflater inflater, ViewGroup parent) {
//    View view = inflater.inflate(R.layout.dialog_grid, parent, false);
//    View outMostView = view.findViewById(R.id.dialogplus_outmost_container);
//    outMostView.setBackgroundResource(backgroundResource);
//    gridView = (GridView) view.findViewById(R.id.dialogplus_list);
//    gridView.setNumColumns(columnNumber);
//    gridView.setOnItemClickListener(this);

//    headerContainer = (ViewGroup) view.findViewById(R.id.dialogplus_header_container);
//    footerContainer = (ViewGroup) view.findViewById(R.id.dialogplus_footer_container);
        View view = inflater.inflate(R.layout.dialog_list, parent, false);
        outMostView = view.findViewById(R.id.dialogplus_outmost_container);
        outMostView.setBackgroundResource(backgroundResource);
        scrollView = (NestedScrollView) view.findViewById(R.id.scrollView);
        gridView = (RecyclerView) view.findViewById(R.id.dialogplus_list);
        mFooterView = (LinearLayout) view.findViewById(R.id.footerView);
        mHeaderView = (LinearLayout) view.findViewById(R.id.headerView);
        gridLayoutManager = new GridLayoutManager(parent.getContext(), columnNumber);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        gridView.setLayoutManager(gridLayoutManager);
        gridView.setNestedScrollingEnabled(false);
//        scrollView.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyListener == null) {
//                    throw new NullPointerException("keyListener should not be null");
//                }
//                return keyListener.onKey(v, keyCode, event);
//            }
//        });
        return view;
    }

    @Override
    public void setOnItemClickListener(OnHolderListener listener) {
        this.listener = listener;
    }

    @Override
    public void setOnKeyListener(View.OnKeyListener keyListener) {
        this.keyListener = keyListener;
    }

    @Override
    public View.OnKeyListener getKeyListener() {
        return keyListener;
    }

    @Override
    public View getInflatedView() {
        return gridView;
    }

    @Override
    public View getHeader() {
        return headerView;
    }

    @Override
    public View getFooter() {
        return footerView;
    }

    @Override
    public View getContentView() {
        return outMostView;
    }
    @Override
    public View getKeyListenerView() {
        return scrollView;
    }
    //  @Override
//  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//    if (listener == null) {
//      return;
//    }
//    listener.onItemClick(parent.getItemAtPosition(position), view, position);
//  }
}
