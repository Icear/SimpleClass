package indi.github.icear.simpleclass.viewmodule.calendarimport;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import indi.github.icear.simpleclass.R;
import indi.github.icear.simpleclass.SimpleClassApplication;
import indi.github.icear.simpleclass.module.academicdata.entity.IClass;
import indi.github.icear.simpleclass.module.calendardata.entity.CalendarInfo;

/**
 *
 */
public class CalendarImportFragment extends Fragment implements CalendarImportContract.View {
    private CalendarImportContract.Presenter mPresenter;
    private RecyclerView mRecyclerView;
    private Toolbar mToolbar;
    private View mProgressBar;

    //    private List<IClass> mItems;
    private List<IClass> mShownItems;
    private List<Boolean> mStatusList;

    public CalendarImportFragment() {
        // Required empty public constructor
    }

    public static CalendarImportFragment newInstance(Bundle bundle) {
        CalendarImportFragment fragment = new CalendarImportFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.onCreate(this.getContext(), this.getArguments());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(indi.github.icear.simpleclass.R.layout.fragment_calendar_import, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(indi.github.icear.simpleclass.R.id.recyclerView_class_process_import);
        mToolbar = (Toolbar) rootView.findViewById(indi.github.icear.simpleclass.R.id.toolbar);
        mProgressBar = rootView.findViewById(indi.github.icear.simpleclass.R.id.progressBar_import);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.onStart();
    }

    @Override
    public void setPresenter(CalendarImportContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showProgress() {
        mToolbar.setTitle(indi.github.icear.simpleclass.R.string.import_course_to_calendar);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mToolbar.setTitle(indi.github.icear.simpleclass.R.string.className);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showWorkingItems(List<IClass> items) {
        mShownItems = new ArrayList<>();
        mStatusList = new ArrayList<>();
        mRecyclerView.setAdapter(new CalendarImportRecyclerViewAdapter(mShownItems, mStatusList));
    }

    @Override
    public void showWorkingItem(IClass item) {
//        mShownItems.add(0,item);
//        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void showItemWorkResult(IClass item, boolean result) {
        mShownItems.add(0, item);
        mStatusList.add(0, result);
        mRecyclerView.getAdapter().notifyItemInserted(0);
    }

    @Override
    public void showProgressSuccessfullyFinished() {
        mToolbar.setTitle(indi.github.icear.simpleclass.R.string.import_finish);
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle(indi.github.icear.simpleclass.R.string.import_finish)
                .setMessage(indi.github.icear.simpleclass.R.string.import_finish_last_sentence)
                .setPositiveButton(R.string.yes, null)
                .create();
        dialog.show();
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
    public void showWarningMessage(String message) {
        AlertDialog alertDialog =
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.app_name)
                        .setMessage(message)
                        .setPositiveButton(R.string.yes, null)
                        .create();
        alertDialog.show();
    }

    @Override
    public void quitAll() {
//        getFragmentManager().popBackStack();
        SimpleClassApplication.getApplication().exitAPP();
    }

    /**
     * 要求用户选择一个日历以导入或创建单独的日历
     *
     * @param calendarInfoList 备选日历
     */
    @Override
    public void chooseOrCreateNewCalendar(final List<CalendarInfo> calendarInfoList) {
        final List<String> itemList = new ArrayList<>();

        for (CalendarInfo calendarInfo :
                calendarInfoList) {
            itemList.add(calendarInfo.getAccountName());
        }

        itemList.add(getString(indi.github.icear.simpleclass.R.string.use_preset_account));

        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setTitle(indi.github.icear.simpleclass.R.string.choose_account_to_write)
                .setItems(itemList.toArray(new String[itemList.size()]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which < calendarInfoList.size()) {
                            mPresenter.onCalendarConfirm(calendarInfoList.get(which));
                        } else {
                            mPresenter.onCalendarConfirm(null);
                        }
                    }
                })
                .create();
        alertDialog.show();
    }
}