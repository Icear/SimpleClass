package indi.github.icear.simpleclass.viewmodule.classdetail;

import java.util.Collections;
import java.util.List;

import indi.github.icear.simpleclass.module.academicdata.entity.IClass;
import indi.github.icear.simpleclass.module.academicdata.entity.IClassInfo;

/**
 * Created by icear on 2017/10/14.
 * ClassDetail的Presenter控制层
 */

class ClassDetailPresenter implements ClassDetailContract.Presenter {
    private ClassDetailContract.View mView;
    private IClass mItem;
    private IClassInfo deletedItem;

    ClassDetailPresenter(ClassDetailContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mView.showItemInfo(mItem);
    }


    @Override
    public void receiveData(IClass item) {
        mItem = item;
    }

    @Override
    public void onUserConfirm() {
        mView.goBackToLastLevel();
    }

    @Override
    public void swipeItem(int position1, int position2) {
        List<IClassInfo> classInfos = mItem.getClassInfo();
        if (0 > position1 || 0 > position2
                || position1 >= classInfos.size() || position2 >= classInfos.size()) {
            throw new IndexOutOfBoundsException("position1: " + position1 + " position2:" + position2);
        }
        Collections.swap(classInfos, position1, position2);
    }

    @Override
    public void delItem(int position) {
        List<IClassInfo> classInfoList = mItem.getClassInfo();
        deletedItem = classInfoList.get(position);
        classInfoList.remove(position);
    }

    @Override
    public void revertItemDel(int position) {
        if (deletedItem != null) {
            List<IClassInfo> classInfos = mItem.getClassInfo();
            classInfos.add(position, deletedItem);
        }
    }
}
