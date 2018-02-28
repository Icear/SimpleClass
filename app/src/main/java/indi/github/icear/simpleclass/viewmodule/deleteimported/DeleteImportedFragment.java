package indi.github.icear.simpleclass.viewmodule.deleteimported;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import indi.github.icear.simpleclass.R;

public class DeleteImportedFragment extends Fragment implements DeleteImportedContract.View {
    private DeleteImportedContract.Presenter mPresenter;
    private View progressBar;
    private Toolbar toolbar;


    public DeleteImportedFragment() {
        // Required empty public constructor
    }

    public static DeleteImportedFragment newInstance(Bundle bundle) {
        return new DeleteImportedFragment();
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_delete_imported, container, false);
        progressBar = rootView.findViewById(R.id.progressBar_delete);
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        return rootView;
    }

    @Override
    public void setPresenter(DeleteImportedContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showWarningMessage(int resId) {
        AlertDialog alertDialog =
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.app_name)
                        .setMessage(resId)
                        .setPositiveButton(R.string.yes, null)
                        .create();
        alertDialog.show();
    }

    @Override
    public void showDeleteResult(int count) {
        AlertDialog alertDialog =
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.app_name)
                        .setMessage(getString(R.string.succeed_delete_event, count))
                        .setPositiveButton(R.string.yes, null)
                        .create();
        alertDialog.show();
    }

    public void goBackToLastLevel() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        toolbar.setTitle(R.string.deleting);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
        toolbar.setTitle(R.string.action_finished);
    }

    @Override
    public void showFailMessage() {
        Dialog dialog = new AlertDialog.Builder(getContext())
                .setTitle(R.string.delete_fail)
                .setMessage(R.string.exception_during_running_cannot_access_calendar)
                .setNegativeButton(R.string.return_last_level, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goBackToLastLevel();
                    }
                })
                .create();
        dialog.show();
    }
}
