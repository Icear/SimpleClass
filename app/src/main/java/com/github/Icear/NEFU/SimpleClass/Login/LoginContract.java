package com.github.Icear.NEFU.SimpleClass.Login;

import com.github.Icear.NEFU.SimpleClass.BasePresenter;
import com.github.Icear.NEFU.SimpleClass.BaseView;


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
