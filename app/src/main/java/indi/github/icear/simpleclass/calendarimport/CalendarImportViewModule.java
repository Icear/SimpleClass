package indi.github.icear.simpleclass.calendarimport;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.transition.Explode;

import indi.github.icear.simpleclass.BaseViewModule;

/**
 * Created by icear on 2017/11/2.
 * CalendarImportViewModule
 */

public class CalendarImportViewModule implements BaseViewModule {
    private CalendarImportFragment fragment;

    @Override
    public void init(Bundle bundle) {
        fragment = CalendarImportFragment.newInstance(bundle);
        fragment.setEnterTransition(new Explode());
        fragment.setExitTransition(new Explode());
        fragment.setReturnTransition(new Explode());
        fragment.setEnterTransition(new Explode());
        new CalendarImportPresenter(fragment);
    }

    @Override
    public Fragment getFragment() {
        return fragment;
    }
}
