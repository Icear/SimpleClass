package com.github.Icear.NEFU.SimpleClass.Welcome;

import com.github.Icear.NEFU.SimpleClass.BasePresenter;
import com.github.Icear.NEFU.SimpleClass.BaseView;

public interface WelcomeContract {
    interface Presenter extends BasePresenter{
        void startFlow();
    }

    interface View extends BaseView<Presenter>{
        void leadToLoginModule();
    }
}
