package indi.github.icear.simpleclass.viewmodule.welcome;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import indi.github.icear.simpleclass.R;
import indi.github.icear.simpleclass.util.ModuleUtil;
import indi.github.icear.simpleclass.viewmodule.deleteimported.DeleteImportedViewModule;
import indi.github.icear.simpleclass.viewmodule.login.LoginViewModule;

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
        View rootView = inflater.inflate(R.layout.fragment_welcome, container, false);
        rootView.findViewById(R.id.button_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPresenter != null) {
                    mPresenter.startFlow();
                }
            }
        });
        //TODO 等待重构，临时乱写的
        rootView.findViewById(R.id.button_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                        .setTitle(R.string.app_name)
                        .setMessage("是否要清除已导入的事件")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                leadToDeleteImportModule();
                            }
                        })
                        .setNegativeButton("否", null)
                        .create();
                alertDialog.show();
            }
        });
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.start();
    }

    @Override
    public void setPresenter(WelcomeContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    public void leadToDeleteImportModule() {
        ModuleUtil.initModule(getActivity().getSupportFragmentManager(), DeleteImportedViewModule.class.getName(), null, true);
    }

    @Override
    public void leadToLoginModule() {
        ModuleUtil.initModule(getActivity().getSupportFragmentManager(), LoginViewModule.class.getName(), null, true);
    }
}
