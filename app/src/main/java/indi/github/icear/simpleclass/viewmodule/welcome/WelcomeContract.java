package indi.github.icear.simpleclass.viewmodule.welcome;

import indi.github.icear.simpleclass.viewmodule.BasePresenter;
import indi.github.icear.simpleclass.viewmodule.BaseView;

interface WelcomeContract {
    interface Presenter extends BasePresenter {
        void startFlow();
    }

    interface View extends BaseView<Presenter> {
        void leadToLoginModule();
    }
}
