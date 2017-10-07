package com.github.icear.nefu.simpleclass.Welcome;

import com.github.icear.nefu.simpleclass.BasePresenter;
import com.github.icear.nefu.simpleclass.BaseView;

public interface WelcomeContract {
    interface Presenter extends BasePresenter{
        void startFlow();
    }

    interface View extends BaseView<Presenter>{
        void leadToLogin();
    }
}
