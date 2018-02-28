package indi.github.icear.simpleclass.viewmodule.classlist;

import android.os.Bundle;

import java.util.List;

import indi.github.icear.simpleclass.module.academicdata.entity.Class;
import indi.github.icear.simpleclass.viewmodule.BasePresenter;
import indi.github.icear.simpleclass.viewmodule.BaseView;

/**
 * Created by icear on 2017/10/7.
 * ClassListViewModule的交互协议接口
 */

interface ClassListContract {
    interface Presenter extends BasePresenter {
        void showItemDetail(Class item);
        void onUserConfirmed();

        void swapItem(int position1, int position2);
        void delItem(int position);

        void revertItemDel(int position);
    }

    interface View extends BaseView<Presenter> {
        void showData(List<Class> itemList);
        void showProgressBar();
        void hideProgressBar();
        void leadToImportModule();

        void initItemDetailModule(Bundle bundle);
        void showMessage(int res);
    }
}
