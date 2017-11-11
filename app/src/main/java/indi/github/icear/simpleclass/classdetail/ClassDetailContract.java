package indi.github.icear.simpleclass.classdetail;

import indi.github.icear.simpleclass.BasePresenter;
import indi.github.icear.simpleclass.BaseView;
import indi.github.icear.simpleclass.data.academicdata.entity.Class;
import indi.github.icear.simpleclass.data.academicdata.entity.ClassInfo;

/**
 * Created by icear on 2017/10/14.
 * ClassDetail模块的MVP协议
 */

interface ClassDetailContract {
    interface Presenter extends BasePresenter {
        void receiveData(Class item);

        void onUserConfirm();

        void swipeItem(int position1, int position2);

        void delItem(int position);

        void revertItemDel(int position);
    }

    interface View extends BaseView<Presenter> {
        void showItemInfo(Class item);

        void showItemModifyView(ClassInfo item);

        void goBackToLastLevel();
    }
}
