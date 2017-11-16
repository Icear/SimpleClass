package indi.github.icear.simpleclass.deleteimported;

import indi.github.icear.simpleclass.BasePresenter;
import indi.github.icear.simpleclass.BaseView;

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
