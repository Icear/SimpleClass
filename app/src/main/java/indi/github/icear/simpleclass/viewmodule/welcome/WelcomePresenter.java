package indi.github.icear.simpleclass.viewmodule.welcome;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

/**
 * Created by icear on 2017/10/7.
 * WelcomePresenter
 */

class WelcomePresenter implements WelcomeContract.Presenter {

    private WelcomeContract.View mWelcomeView;

    WelcomePresenter(@NonNull WelcomeContract.View welcomeView) {
        mWelcomeView = welcomeView;
        mWelcomeView.setPresenter(this);
    }

    @Override
    public void onCreate(Context context, Bundle bundle) {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void startFlow() {
        mWelcomeView.leadToLoginModule();
    }
}
