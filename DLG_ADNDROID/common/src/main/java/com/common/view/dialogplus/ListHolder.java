package com.common.view.dialogplus;

import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import demo.java.com.common.R;


public class ListHolder implements HolderAdapter{

  private int backgroundResource;

  private RecyclerView listView;
  private LinearLayout mFooterView;
  private LinearLayout mHeaderView;
  private LinearLayoutManager layoutManager;
  private OnHolderListener listener;
  private View.OnKeyListener keyListener;
  private View headerView;
  private View footerView;
  private View outMostView;
  private NestedScrollView scrollView;

  @Override
  public void addHeader(View view) {
    if (view == null || mHeaderView == null) {
      return;
    }
    mHeaderView.addView(view);
    headerView = view;
  }

  @Override
  public void addFooter(View view) {
    if (view == null || mFooterView == null ) {
      return;
    }
    mFooterView.addView(view);
    footerView = view;
  }

  @Override
  public void setAdapter(RecyclerView.Adapter adapter) {
    listView.setAdapter(adapter);
  }

  @Override
  public void setBackgroundResource(int colorResource) {
    this.backgroundResource = colorResource;
  }

  @Override
  public View getView(LayoutInflater inflater, ViewGroup parent) {
    View view = inflater.inflate(R.layout.dialog_list, parent, false);
    outMostView = view.findViewById(R.id.dialogplus_outmost_container);
    outMostView.setBackgroundResource(backgroundResource);
    scrollView = (NestedScrollView) view.findViewById(R.id.scrollView);
    listView = (RecyclerView) view.findViewById(R.id.dialogplus_list);
    mFooterView =(LinearLayout) view.findViewById(R.id.footerView);
    mHeaderView = (LinearLayout) view.findViewById(R.id.headerView);
    layoutManager = new LinearLayoutManager(parent.getContext());
    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    listView.setLayoutManager(layoutManager);
    listView.setNestedScrollingEnabled(false);
//    listView.setOnItemClickListener(this);
//    scrollView.setOnKeyListener(new View.OnKeyListener() {
//      @Override
//      public boolean onKey(View v, int keyCode, KeyEvent event) {
//        if (keyListener == null) {
//          throw new NullPointerException("keyListener should not be null");
//        }
//        return keyListener.onKey(v, keyCode, event);
//      }
//    });
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
    return listView;
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
  //
//  @Override
//  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//    if (listener == null) {
//      return;
//    }
//    //ListView counts header as position as well. For consistency we don't
//    listener.onItemClick(
//        parent.getItemAtPosition(position),
//        view,
//        headerView != null ? --position : position
//    );
//  }
}
