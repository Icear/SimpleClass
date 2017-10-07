package com.github.icear.nefu.simpleclass.Welcome;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.icear.nefu.simpleclass.Login.LoginViewModule;
import com.github.icear.nefu.simpleclass.R;
import com.github.icear.nefu.simpleclass.Util.ModuleUtil;

/**
 * A placeholder fragment containing a simple view.
 */
public class WelcomeFragment extends Fragment implements WelcomeContract.View{

    private WelcomeContract.Presenter mPresenter;

    public WelcomeFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static WelcomeFragment newInstance() {
        return new WelcomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        Button button = (Button) rootView.findViewById(R.id.button_start);
        button.setOnClickListener(v -> {
            if(mPresenter != null){
                mPresenter.startFlow();
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(WelcomeContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void leadToLogin() {
        ModuleUtil.initModule(getActivity().getSupportFragmentManager(), LoginViewModule.class.getName(), null, true);
    }
}
