package indi.github.icear.simpleclass.viewmodule.login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import indi.github.icear.simpleclass.R;
import indi.github.icear.simpleclass.SimpleClassApplication;
import indi.github.icear.simpleclass.util.ModuleUtil;
import indi.github.icear.simpleclass.viewmodule.classlist.ClassListViewModule;


public class LoginFragment extends Fragment implements LoginContract.View{

    private LoginContract.Presenter mPresenter;
    private EditText editTextAccount;
    private EditText editTextPassword;
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
    public static LoginFragment newInstance(Bundle bundle) {
        LoginFragment loginFragment = new LoginFragment();
        loginFragment.setArguments(bundle);
        return loginFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.onCreate(this.getContext(), getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(indi.github.icear.simpleclass.R.layout.fragment_login, container, false);
        editTextAccount = (EditText) rootView.findViewById(indi.github.icear.simpleclass.R.id.editText_account);
        editTextPassword = (EditText) rootView.findViewById(indi.github.icear.simpleclass.R.id.editText_password);
        progressBar = (ProgressBar) rootView.findViewById(indi.github.icear.simpleclass.R.id.progressBar_login);
        final Button buttonLogin = (Button) rootView.findViewById(R.id.button_login);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextAccount.getText().toString().trim().isEmpty()) {
                    editTextAccount.setError(getString(indi.github.icear.simpleclass.R.string.enterCorrectAccount));
                    return;
                }
                if (editTextPassword.getText().toString().trim().isEmpty()) {
                    editTextPassword.setError(getString(indi.github.icear.simpleclass.R.string.enterCorrectPassword));
                    return;
                }
                InputMethodManager imm = (InputMethodManager) SimpleClassApplication
                        .getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editTextPassword.getWindowToken(), 0); //强制隐藏键盘
                mPresenter.login(editTextAccount.getText().toString(),
                        editTextPassword.getText().toString());
            }
        });
        editTextAccount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_NEXT:
                        editTextPassword.requestFocus();
                        break;
                }
                return true;
            }
        });
        editTextPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_DONE:
                        buttonLogin.callOnClick();
                        break;
                }
                return true;
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
        mPresenter.onStart();
    }

    @Override
    public void showMessage(String errorMessage) {
        Snackbar.make(editTextPassword, errorMessage, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void showMessage(int resourceID) {
        Snackbar.make(editTextPassword, resourceID, Snackbar.LENGTH_LONG)
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
    public void leadToClassModule(Bundle bundle) {
        ModuleUtil.initModule(getActivity().getSupportFragmentManager(),
                ClassListViewModule.class.getName(), bundle, true);
    }

    private void hideSystemSoftKeyboard() {

    }
}
