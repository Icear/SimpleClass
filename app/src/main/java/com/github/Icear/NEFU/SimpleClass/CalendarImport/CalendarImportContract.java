package com.github.Icear.NEFU.SimpleClass.CalendarImport;

import com.github.Icear.NEFU.SimpleClass.BasePresenter;
import com.github.Icear.NEFU.SimpleClass.BaseView;

/**
 * Created by icear on 2017/11/2.
 * CalendarImportContract
 */

public interface CalendarImportContract {
    interface Presenter extends BasePresenter {
        void startImportProgress();
    }

    interface View extends BaseView<Presenter> {

    }
}
