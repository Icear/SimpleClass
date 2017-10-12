package com.github.Icear.NEFU.SimpleClass.ClassList;

import android.os.AsyncTask;

import com.github.Icear.NEFU.SimpleClass.Data.AcademicDataProvider;
import com.github.Icear.NEFU.SimpleClass.Data.Class.Class;
import com.github.Icear.NEFU.SimpleClass.R;

import java.io.IOException;
import java.util.List;

/**
 * Created by icear on 2017/10/7.
 */

public class ClassListPresenter implements ClassListContract.Presenter {

    private boolean isInited = false;

    private ClassListContract.View mClassListView;

    public ClassListPresenter(ClassListContract.View classListView){
        mClassListView = classListView;
        mClassListView.setPresenter(this);
    }

    @Override
    public void start() {
        if (!isInited) {
            new AsyncTask<Object, Object, List<Class>>() {

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
                protected List<Class> doInBackground(Object... params) {
                    try {
                        return AcademicDataProvider.getInstance().getClassesFromNetwork();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                @Override
                protected void onPostExecute(List<Class> classes) {
                    super.onPostExecute(classes);
                    if (classes != null) {
                        mClassListView.showData(classes);
                        isInited = true;
                    } else {
                        mClassListView.showMessage(R.string.errorInReadClass_PleaseCheckTheInternet);
                    }
                    mClassListView.hideProgressBar();
                }
            }.execute();
        }
    }

    @Override
    public void showItemDetail(Class item) {
        mClassListView.initItemDetailModule(item);
    }

    @Override
    public void onUserConfirmed() {
        mClassListView.leadToImportModule();
    }
}
