package com.github.Icear.NEFU.SimpleClass.CalendarImport;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.Icear.NEFU.SimpleClass.Data.AcademicData.Entity.Class;
import com.github.Icear.NEFU.SimpleClass.Data.CalendarData.Entity.CalendarInfo;
import com.github.Icear.NEFU.SimpleClass.R;
import com.github.Icear.NEFU.SimpleClass.SimpleClassApplication;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class CalendarImportFragment extends Fragment implements CalendarImportContract.View {
    private CalendarImportContract.Presenter mPresenter;
    private RecyclerView mRecyclerView;
    private Toolbar mToolbar;
    private View mProgressBar;

    //    private List<Class> mItems;
    private List<Class> mShownItems;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_calendar_import, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_class_process_import);
        mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        mProgressBar = rootView.findViewById(R.id.progressBar_import);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.start();
    }

    @Override
    public void setPresenter(CalendarImportContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showProgress() {
        mToolbar.setTitle(R.string.import_course_to_calendar);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mToolbar.setTitle(R.string.className);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showWorkingItems(List<Class> items) {
        mShownItems = new ArrayList<>();
        mStatusList = new ArrayList<>();
        mRecyclerView.setAdapter(new CalendarImportRecyclerViewAdapter(mShownItems, mStatusList));
    }

    @Override
    public void showWorkingItem(Class item) {
//        mShownItems.add(0,item);
//        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void showItemWorkResult(Class item, boolean result) {
        mShownItems.add(0, item);
        mStatusList.add(0, result);
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void showProgressFinished() {
        mToolbar.setTitle(R.string.import_finish);
        //TODO 添加更多引导信息
    }

    @Override
    public void showWarningMessage(int resId) {
        AlertDialog alertDialog =
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.app_name)
                        .setMessage(resId)
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

        itemList.add(getString(R.string.use_preset_account));

        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setTitle(R.string.choose_account_to_write)
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