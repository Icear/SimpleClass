package com.github.Icear.NEFU.SimpleClass.Login;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.github.Icear.NEFU.SimpleClass.R;
import com.github.Icear.NEFU.SimpleClass.SimpleClassApplication;

import java.io.IOException;

/**
 * Created by icear on 2017/10/6.
 * LoginPresenter
 */

class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View mLoginView;

    LoginPresenter(@NonNull LoginContract.View loginView) {
        mLoginView = loginView;
        mLoginView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void login(String account, String password) {
        /* 这部分有网络通信，需要放置到子线程中进行 */

        String[] params = new String[2];
        params[0] = account;
        params[1] = password;

        new AsyncTask<String,Object,Boolean>(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mLoginView.showProgressBar();
            }

            @Override
            protected Boolean doInBackground(String... params) {
                try {
                    return SimpleClassApplication.getApplication().getAcademicDataProvider().init(params[0], params[1]);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                mLoginView.hideProgressBar();
                super.onPostExecute(aBoolean);
                if (aBoolean != null) {
                    if(aBoolean){
                        //登陆成功
                        mLoginView.showMessage(R.string.loginSuccessfully);
                        mLoginView.leadToClassModule();
                    }else{
                        //登陆失败
                        mLoginView.showMessage(R.string.accountOrPasswordError);
                    }
                }else{
                    //连接至服务器期间出现错误
                    mLoginView.showMessage(R.string.linkToAcademicError);
                }
            }
        }.execute(params);
    }
}
