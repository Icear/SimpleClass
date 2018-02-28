package indi.github.icear.simpleclass.viewmodule.classdetail;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import indi.github.icear.simpleclass.R;
import indi.github.icear.simpleclass.module.academicdata.AcademicDataProvider;
import indi.github.icear.simpleclass.module.academicdata.entity.Class;
import indi.github.icear.simpleclass.module.academicdata.entity.ClassInfo;


public class ClassDetailFragment extends Fragment implements ClassDetailContract.View,
        ClassInfoRecyclerViewAdapter.ItemActionCallBack,
        ClassDetailItemTouchHelperCallback.ItemModifyActionCallBack {

    public static String PARAMS_CLASS_POSITION = "PARAMS_CLASS_POSITION";
    private ClassDetailContract.Presenter mPresenter;
    private Toolbar toolbar;
    private TextView textViewClassName;
    private TextView textViewTeacher;
    private RecyclerView mRecyclerView;

    public ClassDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ClassDetailFragment.
     */
    public static ClassDetailFragment newInstance(Bundle bundle) {
        ClassDetailFragment fragment = new ClassDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPresenter.receiveData(
                    AcademicDataProvider.getInstance()
                            .getClasses().get(getArguments().getInt(PARAMS_CLASS_POSITION)));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(indi.github.icear.simpleclass.R.layout.fragment_class_detail, container, false);
        toolbar = (Toolbar) rootView.findViewById(indi.github.icear.simpleclass.R.id.toolbar);
        textViewClassName = (TextView) rootView.findViewById(indi.github.icear.simpleclass.R.id.textView_className);
        textViewTeacher = (TextView) rootView.findViewById(indi.github.icear.simpleclass.R.id.textView_teacher);
        mRecyclerView = (RecyclerView) rootView.findViewById(indi.github.icear.simpleclass.R.id.recyclerView_classInfo);
        View fab = rootView.findViewById(indi.github.icear.simpleclass.R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onUserConfirm();
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
    public void setPresenter(ClassDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showItemInfo(Class item) {
        toolbar.setTitle(item.getName());
        textViewClassName.setText(item.getName());
        textViewTeacher.setText(item.getTeachers());
        mRecyclerView.setAdapter(new ClassInfoRecyclerViewAdapter(item.getClassInfo(), this));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ClassDetailItemTouchHelperCallback(this, mRecyclerView.getAdapter()));
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public void showItemModifyView(ClassInfo item) {

    }

    @Override
    public void goBackToLastLevel() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void onItemClick(ClassInfo item) {

    }

    @Override
    public void swapItemData(int position1, int position2) {
        mPresenter.swipeItem(position1, position2);
    }

    @Override
    public void delItemData(final int position) {
        mPresenter.delItem(position);
        Snackbar.make(mRecyclerView, R.string.delete_succeed, Snackbar.LENGTH_LONG)
                .setAction(R.string.undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.revertItemDel(position);
                        mRecyclerView.getAdapter().notifyItemInserted(position);
                    }
                })
                .show();
    }
}
