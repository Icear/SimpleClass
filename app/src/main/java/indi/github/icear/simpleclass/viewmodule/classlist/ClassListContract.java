package indi.github.icear.simpleclass.viewmodule.classlist;

import android.os.Bundle;

import java.util.List;

import indi.github.icear.simpleclass.module.academicdata.entity.IClass;
import indi.github.icear.simpleclass.viewmodule.BasePresenter;
import indi.github.icear.simpleclass.viewmodule.BaseView;

/**
 * Created by icear on 2017/10/7.
 * ClassListViewModule的交互协议接口
 */

interface ClassListContract {
    interface Presenter extends BasePresenter {
        void showItemDetail(IClass item);

        void onUserConfirmClassList();

        void swapItem(int position1, int position2);
        void delItem(int position);

        void revertItemDel(int position);

        void onUserConfirmSection(String section);
    }

    interface View extends BaseView<Presenter> {
        void showData(List<IClass> itemList);
        void showProgressBar();
        void hideProgressBar();

        void leadToImportModule(Bundle bundle);

        void initItemDetailModule(Bundle bundle);
        void showMessage(int res);

        void askForChooseSection(List<String> sectionList);
    }
}
