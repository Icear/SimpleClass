package indi.github.icear.simpleclass.viewmodule.deleteimported;

import indi.github.icear.simpleclass.viewmodule.BasePresenter;
import indi.github.icear.simpleclass.viewmodule.BaseView;

/**
 * Created by icear on 2017/11/16.
 * DeleteImportedContract
 */

interface DeleteImportedContract {
    interface Presenter extends BasePresenter {

    }

    interface View extends BaseView<Presenter> {

        void showWarningMessage(int resId);

        void showDeleteResult(int count);

        void showProgress();

        void hideProgress();

        void showFailMessage();
    }
}
