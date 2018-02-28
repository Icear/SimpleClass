package indi.github.icear.simpleclass.viewmodule.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.transition.Explode;

import indi.github.icear.simpleclass.viewmodule.BaseViewModule;

/**
 * Created by icear on 2017/10/7.
 * LoginViewModule
 */

public class LoginViewModule implements BaseViewModule {
    private LoginFragment fragment;

    @Override
    public void init(Bundle bundle) {
        fragment = LoginFragment.newInstance();
        fragment.setEnterTransition(new Explode());
        fragment.setExitTransition(new Explode());
        fragment.setReturnTransition(new Explode());
        fragment.setEnterTransition(new Explode());
        new LoginPresenter(fragment);
    }

    @Override
    public Fragment getFragment() {
        return fragment;
    }
}
