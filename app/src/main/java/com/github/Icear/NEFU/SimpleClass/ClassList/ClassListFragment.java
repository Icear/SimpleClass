package com.github.Icear.NEFU.SimpleClass.ClassList;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.Icear.NEFU.SimpleClass.ClassDetail.ClassDetailFragment;
import com.github.Icear.NEFU.SimpleClass.ClassDetail.ClassDetailViewModule;
import com.github.Icear.NEFU.SimpleClass.Data.AcademicDataProvider;
import com.github.Icear.NEFU.SimpleClass.Data.Class.Class;
import com.github.Icear.NEFU.SimpleClass.R;
import com.github.Icear.NEFU.SimpleClass.Util.ModuleUtil;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 */
public class ClassListFragment extends Fragment implements ClassListContract.View {

    //TODO 跳转向下一个module的函数未完成
    //TODO 跳转向showItemDetail的函数未完成
    //Done Item右划以删除的功能完成
    //DONE Activity按钮确认事件完成

    private ClassListContract.Presenter mPresenter;
    private RecyclerView mRecyclerView;
    private View mProgressBar;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ClassListFragment() {
    }

    public static ClassListFragment newInstance() {
        return new ClassListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_class_list, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_class);
        mProgressBar = rootView.findViewById(R.id.progressBar_classList);
        View fabButton = rootView.findViewById(R.id.fab);
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPresenter != null) {
                    mPresenter.onUserConfirmed();
                }
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
    public void setPresenter(ClassListContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showData(List<Class> classList) {
        /// Set the adapter
        Context context = mRecyclerView.getContext();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setAdapter(new ClassListRecyclerViewAdapter(classList, new ListActionCallBack()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                getActivity(), DividerItemDecoration.VERTICAL));//添加分割线
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ClassListItemTouchHelperCallback(mRecyclerView));//用于实现向右滑动删除以及上下拖动的功能
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void leadToImportModule() {

    }

    @Override
    public void initItemDetailModule(Class item) {
        int position = AcademicDataProvider.getInstance().getClasses().indexOf(item);
        Bundle bundle = new Bundle();
        bundle.putInt(ClassDetailFragment.PARAMS_CLASS_POSITION, position); //TODO 待改进的传值方式，这里的关联程度太高
        ModuleUtil.initModule(getFragmentManager(), ClassDetailViewModule.class.getName(), bundle, true);
    }

    @Override
    public void showMessage(int resourceID) {
        Snackbar.make(getActivity().findViewById(R.id.container),resourceID,Snackbar.LENGTH_LONG)
                .show();
    }

    class ListActionCallBack {
        void onListItemClick(Class item) {
            mPresenter.showItemDetail(item);
        }
    }



}
