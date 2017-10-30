package com.github.Icear.NEFU.SimpleClass.ClassDetail;

import com.github.Icear.NEFU.SimpleClass.Data.Class.Class;

/**
 * Created by icear on 2017/10/14.
 */

class ClassDetailPresenter implements ClassDetailContract.Presenter {
    private ClassDetailContract.View mView;
    private Class mItem;

    ClassDetailPresenter(ClassDetailContract.View view) {
        mView = view;
    }

    @Override
    public void start() {
        mView.showItemInfo(mItem);
    }


    @Override
    public void receiveData(Class item) {
        mItem = item;
    }
}
