package indi.github.icear.simpleclass.viewmodule.classlist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.transition.Explode;

import indi.github.icear.simpleclass.viewmodule.BaseViewModule;

/**
 * Created by icear on 2017/10/7.
 * ClassListViewModule
 */

public class ClassListViewModule implements BaseViewModule {
    private ClassListFragment fragment;

    @Override
    public void init(Bundle bundle) {
        fragment = ClassListFragment.newInstance(bundle);
        fragment.setEnterTransition(new Explode());
        fragment.setExitTransition(new Explode());
        fragment.setReturnTransition(new Explode());
        fragment.setEnterTransition(new Explode());
        new ClassListPresenter(fragment);
    }

    @Override
    public Fragment getFragment() {
        return fragment;
    }
}
