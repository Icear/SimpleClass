package com.github.Icear.NEFU.SimpleClass.CalendarImport;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.github.Icear.NEFU.SimpleClass.BaseViewModule;

/**
 * Created by icear on 2017/11/2.
 * CalendarImportViewModule
 */

public class CalendarImportViewModule implements BaseViewModule {
    private CalendarImportFragment fragment;

    @Override
    public void init(Bundle bundle) {
        fragment = CalendarImportFragment.newInstance(bundle);
        new CalendarImportPresenter(fragment);
    }

    @Override
    public Fragment getFragment() {
        return fragment;
    }
}
