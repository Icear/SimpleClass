package indi.github.icear.simpleclass.classdetail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.transition.Explode;

import indi.github.icear.simpleclass.BaseViewModule;

/**
 * Created by icear on 2017/10/14.
 * ClassDetail初始化模块，用于链接View和Presenter
 */

public class ClassDetailViewModule implements BaseViewModule {
    private ClassDetailFragment fragment;

    @Override
    public void init(Bundle bundle) {
        fragment = ClassDetailFragment.newInstance(bundle);
        fragment.setEnterTransition(new Explode());
        fragment.setExitTransition(new Explode());
        fragment.setReturnTransition(new Explode());
        fragment.setEnterTransition(new Explode());
        new ClassDetailPresenter(fragment);
    }

    @Override
    public Fragment getFragment() {
        return fragment;
    }
}
