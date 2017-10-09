package com.github.Icear.NEFU.SimpleClass.Login;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.github.Icear.NEFU.SimpleClass.BaseViewModule;

/**
 * Created by icear on 2017/10/7.
 */

public class LoginViewModule implements BaseViewModule {
    private LoginFragment fragment;

    @Override
    public void init(Bundle bundle) {
        fragment = LoginFragment.newInstance();
        new LoginPresenter(fragment);
    }

    @Override
    public Fragment getFragment() {
        return fragment;
    }
}
