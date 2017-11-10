package indi.github.icear.simpleclass.welcome;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import indi.github.icear.simpleclass.BaseViewModule;

/**
 * Created by icear on 2017/10/7.
 * WelcomeViewModule
 */

public class WelcomeViewModule implements BaseViewModule {
    private WelcomeFragment fragment;

    @Override
    public void init(Bundle bundle) {
        fragment = WelcomeFragment.newInstance();
        new WelcomePresenter(fragment);
    }

    @Override
    public Fragment getFragment() {
        return fragment;
    }
}
