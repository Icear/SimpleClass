package com.github.Icear.NEFU.SimpleClass.ClassDetail;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.github.Icear.NEFU.SimpleClass.BaseViewModule;

/**
 * Created by icear on 2017/10/14.
 * ClassDetail初始化模块，用于链接View和Presenter
 */

public class ClassDetailViewModule implements BaseViewModule {
    private ClassDetailFragment fragment;

    @Override
    public void init(Bundle bundle) {
        fragment = ClassDetailFragment.newInstance(bundle);
        new ClassDetailPresenter(fragment);
    }

    @Override
    public Fragment getFragment() {
        return fragment;
    }
}
