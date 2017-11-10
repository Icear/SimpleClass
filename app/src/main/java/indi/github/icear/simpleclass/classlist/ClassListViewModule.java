package indi.github.icear.simpleclass.classlist;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import indi.github.icear.simpleclass.BaseViewModule;

/**
 * Created by icear on 2017/10/7.
 * ClassListViewModule
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
