package com.github.Icear.NEFU.SimpleClass.ClassList;

import com.github.Icear.NEFU.SimpleClass.BasePresenter;
import com.github.Icear.NEFU.SimpleClass.BaseView;
import com.github.Icear.NEFU.SimpleClass.Data.Class.Class;

import java.util.List;

/**
 * Created by icear on 2017/10/7.
 */

public interface ClassListContract {
    interface Presenter extends BasePresenter{
        void showItemDetail(Class item);
        void onUserConfirmed();
    }

    interface View extends BaseView<Presenter>{
        void showData(List<Class> classList);
        void showProgressBar();
        void hideProgressBar();
        void leadToImportModule();
        void initItemDetailModule(Class item);
        void showMessage(int res);
    }
}
