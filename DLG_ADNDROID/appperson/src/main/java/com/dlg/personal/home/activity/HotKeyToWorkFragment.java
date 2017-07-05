package com.dlg.personal.home.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dlg.personal.R;
import com.dlg.personal.base.BaseFragment;

/**
 * 作者：小明
 * 主要功能：
 * 创建时间：2017/6/28 0028 18:01
 */
public class HotKeyToWorkFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_onlylist, container, false);
        return view;
    }
}
