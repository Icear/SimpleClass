package indi.github.icear.simpleclass.viewmodule.login;

import android.os.Bundle;

import indi.github.icear.simpleclass.viewmodule.BasePresenter;
import indi.github.icear.simpleclass.viewmodule.BaseView;


interface LoginContract {
    interface Presenter extends BasePresenter{
        void login(String account, String password);
    }

    interface View extends BaseView<Presenter> {
        void showMessage(String errorMessage);
        void showMessage(int sourceID);
        void showProgressBar();
        void hideProgressBar();

        void leadToClassModule(Bundle bundle);
    }
}
