package com.github.icear.nefu.simpleclass.Welcome;

import android.support.annotation.NonNull;

/**
 * Created by icear on 2017/10/7.
 */

public class WelcomePresenter implements WelcomeContract.Presenter {

    private WelcomeContract.View mWelcomeView;

    public WelcomePresenter(@NonNull WelcomeContract.View welcomeView){
        mWelcomeView = welcomeView;
        mWelcomeView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void startFlow() {
        mWelcomeView.leadToLoginModule();
    }
}
