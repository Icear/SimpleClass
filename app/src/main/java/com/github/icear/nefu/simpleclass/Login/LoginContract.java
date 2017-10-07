package com.github.icear.nefu.simpleclass.Login;

import com.github.icear.nefu.simpleclass.BasePresenter;
import com.github.icear.nefu.simpleclass.BaseView;


public interface LoginContract {
    interface Presenter extends BasePresenter{
        void login(String account, String password);
    }

    interface View extends BaseView<Presenter>{
        void showMessage(String errorMessage);
        void showMessage(int sourceID);
        void showProgressBar();
        void hideProgressBar();
        void leadToClassModule();
    }
}
