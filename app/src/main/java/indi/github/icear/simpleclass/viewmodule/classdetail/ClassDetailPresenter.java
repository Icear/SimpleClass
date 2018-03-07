package indi.github.icear.simpleclass.viewmodule.classdetail;

import android.content.Context;
import android.os.Bundle;

import java.util.Collections;
import java.util.List;

import indi.github.icear.simpleclass.module.academicdata.AcademicDataProvider;
import indi.github.icear.simpleclass.module.academicdata.entity.IClass;
import indi.github.icear.simpleclass.module.academicdata.entity.IClassInfo;

import static indi.github.icear.simpleclass.viewmodule.classdetail.ClassDetailFragment.PARAMS_CLASS_POSITION;

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
    public void onCreate(Context context, Bundle bundle) {
        if (bundle != null) {
            AcademicDataProvider academicDataProvider = (AcademicDataProvider) bundle.getSerializable("AcademicDataProvider");
            int index = bundle.getInt(PARAMS_CLASS_POSITION);
            mItem = academicDataProvider != null ? academicDataProvider.getClasses().get(index) : null;
        }
    }

    @Override
    public void onStart() {
        mView.showItemInfo(mItem);
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
