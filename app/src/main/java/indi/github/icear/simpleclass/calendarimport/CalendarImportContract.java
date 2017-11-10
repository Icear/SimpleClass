package indi.github.icear.simpleclass.calendarimport;

import android.support.annotation.Nullable;

import java.util.List;

import indi.github.icear.simpleclass.BasePresenter;
import indi.github.icear.simpleclass.BaseView;
import indi.github.icear.simpleclass.data.academicdata.entity.Class;
import indi.github.icear.simpleclass.data.calendardata.entity.CalendarInfo;

/**
 * Created by icear on 2017/11/2.
 * CalendarImportContract
 */

interface CalendarImportContract {
    interface Presenter extends BasePresenter {
//        void startImportProgress();

        /**
         * 当用户确认要导入课程的日历时触发
         *
         * @param calendar 日历对象，null时将创建新日历
         */
        void onCalendarConfirm(@Nullable CalendarInfo calendar);
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

        /**
         * 要求用户选择一个日历以导入或创建单独的日历
         *
         * @param calendarInfoList 备选日历
         */
        void chooseOrCreateNewCalendar(List<CalendarInfo> calendarInfoList);
    }
}
