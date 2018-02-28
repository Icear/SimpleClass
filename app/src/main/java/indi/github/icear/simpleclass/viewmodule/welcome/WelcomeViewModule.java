package indi.github.icear.simpleclass.viewmodule.welcome;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.transition.Explode;

import indi.github.icear.simpleclass.viewmodule.BaseViewModule;

/**
 * Created by icear on 2017/10/7.
 * WelcomeViewModule
 */

public class WelcomeViewModule implements BaseViewModule {
    private WelcomeFragment fragment;

    @Override
    public void init(Bundle bundle) {
        fragment = WelcomeFragment.newInstance();
        fragment.setEnterTransition(new Explode());
        fragment.setExitTransition(new Explode());
        fragment.setReturnTransition(new Explode());
        fragment.setEnterTransition(new Explode());
        new WelcomePresenter(fragment);
    }

    @Override
    public Fragment getFragment() {
        return fragment;
    }
}
