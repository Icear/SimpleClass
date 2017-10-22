package com.github.Icear.NEFU.SimpleClass.Welcome;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.Icear.NEFU.SimpleClass.Login.LoginViewModule;
import com.github.Icear.NEFU.SimpleClass.R;
import com.github.Icear.NEFU.SimpleClass.Util.ModuleUtil;

/**
 * A placeholder fragment containing a simple view.
 */
public class WelcomeFragment extends Fragment implements WelcomeContract.View{

    private WelcomeContract.Presenter mPresenter;
    private Toolbar toolBar;

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
        rootView.findViewById(R.id.button_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPresenter != null) {
                    mPresenter.startFlow();
                }
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
    public void leadToLoginModule() {
        ModuleUtil.initModule(getActivity().getSupportFragmentManager(), LoginViewModule.class.getName(), null, true);
    }
}
