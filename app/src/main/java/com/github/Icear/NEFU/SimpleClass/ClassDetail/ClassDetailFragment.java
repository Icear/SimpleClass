package com.github.Icear.NEFU.SimpleClass.ClassDetail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.Icear.NEFU.SimpleClass.Data.AcademicData.Entity.Class;
import com.github.Icear.NEFU.SimpleClass.Data.AcademicData.Entity.ClassInfo;
import com.github.Icear.NEFU.SimpleClass.R;
import com.github.Icear.NEFU.SimpleClass.SimpleClassApplication;


public class ClassDetailFragment extends Fragment implements ClassDetailContract.View, ClassInfoRecyclerViewAdapter.ItemActionCallBack {

    public static String PARAMS_CLASS_POSITION = "PARAMS_CLASS_POSITION";
    private ClassDetailContract.Presenter mPresenter;
    private Toolbar toolbar;
    private TextView textViewClassName;
    private TextView textViewTeacher;
    private RecyclerView classInfoRecyclerView;

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
                    SimpleClassApplication.getAcademicDataProvider()
                            .getClasses().get(getArguments().getInt(PARAMS_CLASS_POSITION)));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_class_detail, container, false);
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        textViewClassName = (TextView) rootView.findViewById(R.id.textView_className);
        textViewTeacher = (TextView) rootView.findViewById(R.id.textView_teacher);
        classInfoRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_classInfo);
        View fab = rootView.findViewById(R.id.fab);
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
        classInfoRecyclerView.setAdapter(new ClassInfoRecyclerViewAdapter(item.getClassInfo(), this));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ClassInfoItemTouchHelperCallback(classInfoRecyclerView));
        itemTouchHelper.attachToRecyclerView(classInfoRecyclerView);
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
}
