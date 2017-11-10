package indi.github.icear.simpleclass.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import indi.github.icear.simpleclass.classlist.ClassListViewModule;
import indi.github.icear.simpleclass.util.ModuleUtil;


public class LoginFragment extends Fragment implements LoginContract.View{

    private LoginContract.Presenter mPresenter;
    private EditText editText_login;
    private EditText editText_password;
    private ProgressBar progressBar;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoginFragment.
     */
    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(indi.github.icear.simpleclass.R.layout.fragment_login, container, false);
        editText_login = (EditText) rootView.findViewById(indi.github.icear.simpleclass.R.id.editText_account);
        editText_password = (EditText) rootView.findViewById(indi.github.icear.simpleclass.R.id.editText_password);
        progressBar = (ProgressBar) rootView.findViewById(indi.github.icear.simpleclass.R.id.progressBar_login);
        rootView.findViewById(indi.github.icear.simpleclass.R.id.button_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText_login.getText().toString().trim().isEmpty()){
                    editText_login.setError(getString(indi.github.icear.simpleclass.R.string.enterCorrectAccount));
                    return;
                }
                if(editText_password.getText().toString().trim().isEmpty()){
                    editText_password.setError(getString(indi.github.icear.simpleclass.R.string.enterCorrectPassword));
                    return;
                }
                mPresenter.login(editText_login.getText().toString(),
                        editText_password.getText().toString());
            }
        });
        return rootView;
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.start();
    }

    @Override
    public void showMessage(String errorMessage) {
        Snackbar.make(getActivity().findViewById(indi.github.icear.simpleclass.R.id.container), errorMessage, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void showMessage(int resourceID) {
        Snackbar.make(getActivity().findViewById(indi.github.icear.simpleclass.R.id.container), resourceID, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void leadToClassModule() {
        ModuleUtil.initModule(getActivity().getSupportFragmentManager(),
                ClassListViewModule.class.getName(), null, true);
    }
}
