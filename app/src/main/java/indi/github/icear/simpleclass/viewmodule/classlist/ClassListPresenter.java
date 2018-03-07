package indi.github.icear.simpleclass.viewmodule.classlist;

import android.content.Context;
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
//TODO 重构
class ClassListPresenter implements ClassListContract.Presenter {

    private ClassListContract.View mClassListView;
    private IClass deletedItem;
    private AcademicDataProvider academicDataProvider;
    private String section;


    ClassListPresenter(ClassListContract.View classListView) {
        mClassListView = classListView;
        mClassListView.setPresenter(this);
    }

    @Override
    public void onCreate(Context context, Bundle bundle) {
        if (bundle != null) {
            academicDataProvider = (AcademicDataProvider) bundle.getSerializable("AcademicDataProvider");
        }
    }

    @Override
    public void onStart() {
        if (academicDataProvider.getClasses() != null) {
            //已经获取过课程数据，直接从本地读取
            mClassListView.showData(academicDataProvider.getClasses());
        } else {
            //调用函数初始化数据
            loadSectionList();
        }
    }

    @Override
    public void showItemDetail(IClass item) {
        int position = academicDataProvider.getClasses().indexOf(item);
        Bundle bundle = new Bundle();
        bundle.putSerializable("AcademicDataProvider", academicDataProvider);
        bundle.putInt(ClassDetailFragment.PARAMS_CLASS_POSITION, position);
        mClassListView.initItemDetailModule(bundle);
    }

    @Override
    public void onUserConfirmClassList() {
        Bundle bundle = new Bundle();
        bundle.putString("school", academicDataProvider.getSchool());
        bundle.putString("section", section);
        bundle.putSerializable("AcademicDataProvider", academicDataProvider);
        mClassListView.leadToImportModule(bundle);
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

    private void loadSectionList() {
        new LoadAvailableSectionListAsyncTask().execute();
    }

    private void onLoadSectionListSucceed(List<String> sectionList) {
        mClassListView.askForChooseSection(sectionList);
    }

    @Override
    public void onUserConfirmSection(String section) {
        this.section = section;
        showClasses(section);
    }

    private void showClasses(String section) {
        new LoadClassesAsyncTask(section).execute();
    }

    private class LoadAvailableSectionListAsyncTask extends AsyncTask<Object, Object, List<String>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mClassListView.showProgressBar();
            mClassListView.showMessage(R.string.loading_section_list);
        }

        @Override
        protected List<String> doInBackground(Object... params) {
            try {
                return academicDataProvider.getSectionList();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            super.onPostExecute(strings);
            mClassListView.hideProgressBar();
            onLoadSectionListSucceed(strings);
        }
    }

    private class LoadClassesAsyncTask extends AsyncTask<Object, Object, List<IClass>> {
        private String section;

        LoadClassesAsyncTask(String section) {
            this.section = section;
        }

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
                return academicDataProvider.getClassesFromNetwork(section);
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
    }

}
