package com.github.Icear.NEFU.SimpleClass.ClassDetail;

import com.github.Icear.NEFU.SimpleClass.BasePresenter;
import com.github.Icear.NEFU.SimpleClass.BaseView;
import com.github.Icear.NEFU.SimpleClass.Data.Class.Class;
import com.github.Icear.NEFU.SimpleClass.Data.Class.ClassInfo;

/**
 * Created by icear on 2017/10/14.
 * ClassDetail模块的MVP协议
 */

interface ClassDetailContract {
    interface Presenter extends BasePresenter{
        void receiveData(Class item);
    }

    interface View extends BaseView<Presenter>{
        void showItemInfo(Class item);

        void showItemModifyView(ClassInfo item);
    }
}
