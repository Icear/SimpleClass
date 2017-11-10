package indi.github.icear.simpleclass.welcome;

import indi.github.icear.simpleclass.BasePresenter;
import indi.github.icear.simpleclass.BaseView;

interface WelcomeContract {
    interface Presenter extends BasePresenter {
        void startFlow();
    }

    interface View extends BaseView<Presenter> {
        void leadToLoginModule();
    }
}
