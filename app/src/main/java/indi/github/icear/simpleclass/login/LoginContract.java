package indi.github.icear.simpleclass.login;

import indi.github.icear.simpleclass.BasePresenter;
import indi.github.icear.simpleclass.BaseView;


interface LoginContract {
    interface Presenter extends BasePresenter{
        void login(String account, String password);
    }

    interface View extends BaseView<Presenter> {
        void showMessage(String errorMessage);
        void showMessage(int sourceID);
        void showProgressBar();
        void hideProgressBar();
        void leadToClassModule();
    }
}
