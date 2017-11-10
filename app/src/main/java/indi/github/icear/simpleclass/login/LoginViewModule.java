package indi.github.icear.simpleclass.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import indi.github.icear.simpleclass.BaseViewModule;

/**
 * Created by icear on 2017/10/7.
 * LoginViewModule
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
