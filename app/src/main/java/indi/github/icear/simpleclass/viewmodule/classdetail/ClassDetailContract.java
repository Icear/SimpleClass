package indi.github.icear.simpleclass.viewmodule.classdetail;

import indi.github.icear.simpleclass.module.academicdata.entity.IClass;
import indi.github.icear.simpleclass.module.academicdata.entity.IClassInfo;
import indi.github.icear.simpleclass.viewmodule.BasePresenter;
import indi.github.icear.simpleclass.viewmodule.BaseView;

/**
 * Created by icear on 2017/10/14.
 * ClassDetail模块的MVP协议
 */

interface ClassDetailContract {
    interface Presenter extends BasePresenter {
        void onUserConfirm();

        void swipeItem(int position1, int position2);

        void delItem(int position);

        void revertItemDel(int position);
    }

    interface View extends BaseView<Presenter> {
        void showItemInfo(IClass item);

        void showItemModifyView(IClassInfo item);

        void goBackToLastLevel();
    }
}
