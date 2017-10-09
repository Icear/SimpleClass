package com.github.Icear.NEFU.SimpleClass.ClassList;

/**
 * Created by icear on 2017/10/7.
 */

public class ClassListPresenter implements ClassListContract.Presenter {

    private ClassListContract.View mClassListView;

    public ClassListPresenter(ClassListContract.View classListView){
        mClassListView = classListView;
        mClassListView.setPresenter(this);
    }

    @Override
    public void start() {

    }
}
