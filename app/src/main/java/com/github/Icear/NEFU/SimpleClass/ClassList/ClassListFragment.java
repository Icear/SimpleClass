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

import com.github.Icear.NEFU.SimpleClass.CalendarImport.CalendarImportViewModule;
import com.github.Icear.NEFU.SimpleClass.ClassDetail.ClassDetailViewModule;
import com.github.Icear.NEFU.SimpleClass.Data.AcademicData.Entity.Class;
import com.github.Icear.NEFU.SimpleClass.R;
import com.github.Icear.NEFU.SimpleClass.Util.ModuleUtil;
import com.github.Icear.NEFU.SimpleClass.Util.RandomColorUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 */
public class ClassListFragment extends Fragment implements ClassListContract.View, ClassListRecyclerViewAdapter.ListActionCallBack, ClassListItemTouchHelperCallback.ItemModifyActionCallBack {

    //TODO 跳转向下一个module的函数未完成
    //Done 跳转向showItemDetail的函数未完成
    //Done Item右划以删除的功能完成
    //Done Activity按钮确认事件完成

    private ClassListContract.Presenter mPresenter;
    private RecyclerView mRecyclerView;
    private View mProgressBar;
    private List<Integer> mColorList;//用于储存RecycleViewItem的icon颜色

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
    public void showData(List<Class> itemList) {
        /// Set the adapter

        if (mColorList == null) {
            //初始化mColorList
            mColorList = new ArrayList<>();
        }
        if (mColorList.size() != itemList.size()) {
            //如果两者数量不相等，判断数据发生了变化，重新生成颜色
            mColorList.clear();
            for (int i = 0; i < itemList.size(); i++) {
                //根据itemList数目生成对应数量的Color
                mColorList.add(RandomColorUtil.getRandomColor());//生成随机的颜色
            }
        }

        Context context = mRecyclerView.getContext();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setAdapter(new ClassListRecyclerViewAdapter(itemList, mColorList, this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                getActivity(), DividerItemDecoration.VERTICAL));//添加分割线
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ClassListItemTouchHelperCallback(this));//用于实现向右滑动删除以及上下拖动的功能
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
        ModuleUtil.initModule(getFragmentManager(), CalendarImportViewModule.class.getName(), null, true);
    }

    @Override
    public void initItemDetailModule(Bundle bundle) {
        ModuleUtil.initModule(getFragmentManager(), ClassDetailViewModule.class.getName(), bundle, true);
    }

    @Override
    public void showMessage(int resourceID) {
        Snackbar.make(getActivity().findViewById(R.id.container), resourceID, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void onListItemClick(Class item) {
        mPresenter.showItemDetail(item);
    }

    @Override
    public void swapItem(int position1, int position2) {
        mPresenter.swapItem(position1, position2);
        Collections.swap(mColorList, position1, position2);//参数检查由Presenter进行，这里只做基本的View层反馈
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void delItem(int position) {
        mPresenter.delItem(position);
        mColorList.remove(position);//参数检查由Presenter进行，这里只做基本的View层反馈
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }
}
