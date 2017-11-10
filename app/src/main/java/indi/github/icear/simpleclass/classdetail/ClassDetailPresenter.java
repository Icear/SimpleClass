package indi.github.icear.simpleclass.classdetail;

import indi.github.icear.simpleclass.data.academicdata.entity.Class;

/**
 * Created by icear on 2017/10/14.
 * ClassDetail的Presenter控制层
 */

class ClassDetailPresenter implements ClassDetailContract.Presenter {
    private ClassDetailContract.View mView;
    private Class mItem;

    ClassDetailPresenter(ClassDetailContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mView.showItemInfo(mItem);
    }


    @Override
    public void receiveData(Class item) {
        mItem = item;
    }

    @Override
    public void onUserConfirm() {
        mView.goBackToLastLevel();
    }
}
