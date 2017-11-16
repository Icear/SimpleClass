package indi.github.icear.simpleclass.deleteimported;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import indi.github.icear.simpleclass.MainActivity;
import indi.github.icear.simpleclass.SimpleClassApplication;

/**
 * Created by icear on 2017/11/16.
 * DeleteImportedPresenter
 */

//TODO 临时写出来的功能，先凑合用吧，代码之后再重构，重复部分太多

class DeleteImportedPresenter implements DeleteImportedContract.Presenter, MainActivity.OnRequestPermissionResultCallback {
    private static final int REQUEST_CODE_REQUEST_PERMISSION = 126;

    private DeleteImportedContract.View mView;

    private boolean isRunning = false; //防止重复执行
    private boolean isFinished = false;//防止在操作结束后再次触发


    DeleteImportedPresenter(DeleteImportedContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        if (!isRunning && !isFinished) {
            isRunning = true;
            if (!checkPermission()) {
                requestPermission();
            } else {
                deleteEventsIdentifiedByOrganizer();
            }
        }
    }

    /**
     * 检查相关权限
     *
     * @return 是否有权限
     */
    private boolean checkPermission() {
         /* 权限检查 */
        return !(ActivityCompat.checkSelfPermission(SimpleClassApplication.getApplication(), Manifest.permission.WRITE_CALENDAR) !=
                PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(SimpleClassApplication.getApplication(), Manifest.permission.READ_CALENDAR) !=
                PackageManager.PERMISSION_GRANTED);
    }

    /**
     * 请求相关权限
     */
    private void requestPermission() {
        String[] permissions = new String[]{
                Manifest.permission.WRITE_CALENDAR,
                Manifest.permission.READ_CALENDAR
        };
        MainActivity activity = MainActivity.getInstance();
        activity.setCallback(this);
        ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE_REQUEST_PERMISSION);
    }

    @Override
    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        boolean GRANT_PERMISSION_READ_CALENDAR = false;
        boolean GRANT_PERMISSION_WRITE_CALENDAR = false;

        switch (requestCode) {
            case REQUEST_CODE_REQUEST_PERMISSION:
                for (int i = 0; i < permissions.length; i++) {
                    if (Manifest.permission.READ_CALENDAR.equals(permissions[i])) {
                        GRANT_PERMISSION_READ_CALENDAR = (grantResults[i] == PackageManager.PERMISSION_GRANTED);
                    } else if (Manifest.permission.WRITE_CALENDAR.equals(permissions[i])) {
                        GRANT_PERMISSION_WRITE_CALENDAR = (grantResults[i] == PackageManager.PERMISSION_GRANTED);
                    }
                }
                isRunning = false;
                if (GRANT_PERMISSION_READ_CALENDAR && GRANT_PERMISSION_WRITE_CALENDAR) {
                    start();
                } else {
                    //没有权限时的处理
                    mView.showWarningMessage(indi.github.icear.simpleclass.R.string.oh_no_we_cant_access_your_calendar_app_exit);
//                    mView.quitAll();
                }
                break;
        }
    }

    private void deleteEventsIdentifiedByOrganizer() throws SecurityException {
        new deleteTask(SimpleClassApplication.getApplication().getContentResolver()).execute();
    }

    private class deleteTask extends AsyncTask<Object, Object, Integer> {
        //标记需要和CalendarImportPresenter中CalendarEventAsyncTask中doInBackground函数的organizer相同
        String organizer = "SimpleClass@gmail.com";
        private ContentResolver cr;

        deleteTask(ContentResolver cr) {
            this.cr = cr;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mView.showProgress();
        }

        @Override
        protected Integer doInBackground(Object... params) {
            /*
             * 读取organizer为定义值的事件，调用remove函数删除之
             */

            Uri uri = CalendarContract.Events.CONTENT_URI;


            //TODO 区分创建在用户定义的日历中和创建在自己设置的日历中两种情况下的删除
            /*
             * 问题：
             *  在用户自定义日历中，APP不是同步适配器身份，不能够直接完整地删除数据，只能标记为dirty然后等待该日历的控制器来更新
             *  假设这个日历控制器是尽责的，我们插入了日历又在删除时标记了它，OK
             *  ==================================================================
             *  在自定义日历中，则需要以同步适配器身份来进行删除，不然仅仅是作标记的话，没有相应的同步适配器来更新（因为我就是同步适配器）
             *  所以说需要尽到责任将它一次性清理干净，避免多次插入删除导致垃圾数据越来越多
             *  ==================================================================
             *  还有一个问题，我并不清楚标记之后是不是只能由同步适配器来进行清理，还是说系统会自动清理，有待研究
             *  ==================================================================
             *  目前暂时都以普通身份做标记，等有时间了再仔细研究
             */
//            //以同步适配器身份操作日历
//            Uri calendarUri = CalendarContract.Events.CONTENT_URI.buildUpon()
//                    .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
//                    .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, calendarInfo.getAccountName())
//                    .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL)
//                    .build();

            String selection = "((" + CalendarContract.Events.ORGANIZER + " = ?))";
            String[] selectionArgs = new String[]{organizer};
            try {
                return cr.delete(uri, selection, selectionArgs);
            } catch (SecurityException e) {
                //捕获权限异常
                e.printStackTrace();
                return -1;
            }
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer != -1) {
                //操作成功
                isFinished = true;
                isRunning = false;
                mView.hideProgress();
                mView.showDeleteResult(integer);
            } else {
                //操作失败
                isRunning = false;
                mView.showFailMessage();
            }
        }
    }
}
