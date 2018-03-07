package indi.github.icear.simpleclass.viewmodule.login;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;

import java.io.IOException;

import indi.github.icear.simpleclass.R;
import indi.github.icear.simpleclass.module.academicdata.AcademicDataProvider;

/**
 * Created by icear on 2017/10/6.
 * LoginPresenter
 */

class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View mLoginView;
    private AcademicDataProvider academicDataProvider = new AcademicDataProvider();

    LoginPresenter(@NonNull LoginContract.View loginView) {
        mLoginView = loginView;
        mLoginView.setPresenter(this);
    }

    @Override
    public void onCreate(Context context, Bundle bundle) {

    }

    @Override
    public void onStart() {

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

                    return academicDataProvider.init(params[0], params[1]);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                } catch (IllegalAccessException | ClassNotFoundException | InstantiationException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e.getMessage());
                    //这种异常属于配置问题，不应该进行处理
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
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("AcademicDataProvider", academicDataProvider);
                        mLoginView.leadToClassModule(bundle);
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
