package com.common.view.dialogplus;

import android.support.v7.widget.RecyclerView;

public interface HolderAdapter extends Holder {

  void setAdapter(RecyclerView.Adapter adapter);

  void setOnItemClickListener(OnHolderListener listener);
}
