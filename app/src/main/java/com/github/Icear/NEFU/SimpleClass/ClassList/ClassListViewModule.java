package com.github.Icear.NEFU.SimpleClass.ClassList;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.github.Icear.NEFU.SimpleClass.BaseViewModule;

/**
 * Created by icear on 2017/10/7.
 */

public class ClassListViewModule implements BaseViewModule {
    private ClassListFragment fragment;

    @Override
    public void init(Bundle bundle) {
        fragment = ClassListFragment.newInstance();
        new ClassListPresenter(fragment);
    }

    @Override
    public Fragment getFragment() {
        return fragment;
    }
}
