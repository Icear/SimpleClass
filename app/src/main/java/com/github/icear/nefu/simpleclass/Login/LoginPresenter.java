package com.github.icear.nefu.simpleclass.Login;

import android.support.annotation.NonNull;

import com.github.Icear.NEFU.AcademicAdmin;
import com.github.icear.nefu.simpleclass.Data.AcademicDataProvider;
import com.github.icear.nefu.simpleclass.R;

import java.io.IOException;

/**
 * Created by icear on 2017/10/6.
 */

public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View mLoginView;

    public LoginPresenter(@NonNull LoginContract.View loginView){
        mLoginView = loginView;
        mLoginView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void login(String account, String password) {
        mLoginView.showProgressBar();

        /* 这部分有网络通信，需要放置到子线程中进行 */
        try {
            if(AcademicDataProvider.getInstance().init(account,password)){
                //登陆成功
                mLoginView.hideProgressBar();
            }else{
                //登陆失败
                mLoginView.hideProgressBar();
                mLoginView.showErrorMessage(R.string.accountOrPasswordError);
            }
        } catch (IOException e) {
            e.printStackTrace();
            mLoginView.hideProgressBar();
            mLoginView.showErrorMessage(R.string.linktoAcademicError);
        }
    }
}
