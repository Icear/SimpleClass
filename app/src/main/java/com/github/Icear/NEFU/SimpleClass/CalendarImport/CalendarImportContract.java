package com.github.Icear.NEFU.SimpleClass.CalendarImport;

import com.github.Icear.NEFU.SimpleClass.BasePresenter;
import com.github.Icear.NEFU.SimpleClass.BaseView;
import com.github.Icear.NEFU.SimpleClass.Data.AcademicData.Entity.Class;

import java.util.List;

/**
 * Created by icear on 2017/11/2.
 * CalendarImportContract
 */

interface CalendarImportContract {
    interface Presenter extends BasePresenter {
//        void startImportProgress();
    }

    interface View extends BaseView<Presenter> {

        void showProgress();

        void hideProgress();

        void showWorkingItems(List<Class> items);

        void showWorkingItem(Class item);

        void showItemWorkResult(Class item, boolean result);

        void showProgressFinished();

        void showWarningMessage(int resId);

        void quitAll();
    }
}
