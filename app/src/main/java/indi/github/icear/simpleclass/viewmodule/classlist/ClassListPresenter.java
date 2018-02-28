package indi.github.icear.simpleclass.viewmodule.classlist;

import android.os.AsyncTask;
import android.os.Bundle;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import indi.github.icear.simpleclass.R;
import indi.github.icear.simpleclass.module.academicdata.AcademicDataProvider;
import indi.github.icear.simpleclass.module.academicdata.entity.IClass;
import indi.github.icear.simpleclass.viewmodule.classdetail.ClassDetailFragment;

/**
 * Created by icear on 2017/10/7.
 * ClassListPresenter
 */

class ClassListPresenter implements ClassListContract.Presenter {

    private ClassListContract.View mClassListView;
    private IClass deletedItem;
    private AcademicDataProvider academicDataProvider = AcademicDataProvider.getInstance();

    ClassListPresenter(ClassListContract.View classListView) {
        mClassListView = classListView;
        mClassListView.setPresenter(this);
    }

    @Override
    public void start() {
        if (academicDataProvider.getClasses() != null) {
            //已经获取过课程数据，直接从本地读取
            mClassListView.showData(AcademicDataProvider.getInstance().getClasses());
        } else {
            //调用函数初始化数据
            new AsyncTask<Object, Object, List<IClass>>() {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    mClassListView.showProgressBar();
                    mClassListView.showMessage(R.string.processingClass_PleaseWait);
                }

                /**
                 * Override this method to perform a computation on a background thread. The
                 * specified parameters are the parameters passed to {@link #execute}
                 * by the caller of this task.
                 * <p>
                 * This method can call {@link #publishProgress} to publish updates
                 * on the UI thread.
                 *
                 * @param params The parameters of the task.
                 * @return A result, defined by the subclass of this task.
                 * @see #onPreExecute()
                 * @see #onPostExecute
                 * @see #publishProgress
                 */
                @Override
                protected List<IClass> doInBackground(Object... params) {
                    try {
                        return academicDataProvider.getClassesFromNetwork();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                @Override
                protected void onPostExecute(List<IClass> classes) {
                    super.onPostExecute(classes);
                    if (classes != null) {
                        mClassListView.showData(classes);
                    } else {
                        mClassListView.showMessage(R.string.errorInReadClass_PleaseCheckTheInternet);
                    }
                    mClassListView.hideProgressBar();
                }
            }.execute();
        }
    }

    @Override
    public void showItemDetail(IClass item) {
        int position = academicDataProvider.getClasses().indexOf(item);
        Bundle bundle = new Bundle();
        bundle.putInt(ClassDetailFragment.PARAMS_CLASS_POSITION, position);
        mClassListView.initItemDetailModule(bundle);
    }

    @Override
    public void onUserConfirmed() {
        mClassListView.leadToImportModule();
    }

    @Override
    public void swapItem(int position1, int position2) {
        List<IClass> classes = academicDataProvider.getClasses();
        if (0 > position1 || 0 > position2
                || position1 >= classes.size() || position2 >= classes.size()) {
            throw new IndexOutOfBoundsException("position1: " + position1 + " position2:" + position2);
        }
        Collections.swap(classes, position1, position2);
    }

    @Override
    public void delItem(int position) {
        List<IClass> classes = academicDataProvider.getClasses();
        if (0 > position || position >= classes.size()) {
            throw new IndexOutOfBoundsException("position: " + position);
        }
        deletedItem = classes.get(position);//备份Item以供Revert函数调用
        classes.remove(position);
    }

    @Override
    public void revertItemDel(int position) {
        if (deletedItem != null) {
            List<IClass> classes = academicDataProvider.getClasses();
            classes.add(position, deletedItem);
        }
    }


}
