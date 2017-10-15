package com.github.Icear.NEFU.SimpleClass.ClassList;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.Icear.NEFU.SimpleClass.Data.Class.Class;
import com.github.Icear.NEFU.SimpleClass.R;

import java.util.List;

import static android.support.v7.widget.helper.ItemTouchHelper.DOWN;
import static android.support.v7.widget.helper.ItemTouchHelper.RIGHT;
import static android.support.v7.widget.helper.ItemTouchHelper.UP;

/**
 * A fragment representing a list of Items.
 * <p/>
 */
public class ClassListFragment extends Fragment implements ClassListContract.View {

    //TODO 跳转向下一个module的函数未完成
    //TODO 跳转向showItemDetail的函数未完成
    //Done Item右划以删除的功能完成
    //DONE Activity按钮确认事件完成
    //TODO 在onResume对Activity属性进行设置的话，就要在切换Module的时候重置所有属性，考虑是在切换时进行

    private ClassListContract.Presenter mPresenter;
    private RecyclerView mRecyclerView;
    private View mProgressBar;
    private Toolbar toolbar;

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
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        FloatingActionButton fabButton = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fabButton.setImageResource(R.drawable.ic_check_black_24dp);
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPresenter != null) {
                    mPresenter.onUserConfirmed();
                }
            }
        });
        fabButton.setVisibility(View.VISIBLE);

        return rootView;
    }


//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnListFragmentInteractionListener) {
//            mListener = (OnListFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnListFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    @Override
    public void onResume() {
        super.onResume();
        toolbar.setTitle(R.string.checkYourCourse);
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
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(UP | DOWN , RIGHT);//允许上下拖拽，允许右滑
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                ClassListRecyclerViewAdapter adapter = (ClassListRecyclerViewAdapter) recyclerView.getAdapter();
                adapter.swapItem(viewHolder.getAdapterPosition(),target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == RIGHT) {
                    //在这里选择删除
                    ClassListRecyclerViewAdapter adapter = (ClassListRecyclerViewAdapter) mRecyclerView.getAdapter();
                    adapter.delItem(viewHolder.getAdapterPosition());
                }
            }
        });//用于实现向右滑动删除功能
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
        //TODO 想办法把这段代码抽出去，放在这里混合了
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

    }

    @Override
    public void showMessage(int resourceID) {
        Snackbar.make(getActivity().findViewById(R.id.container),resourceID,Snackbar.LENGTH_LONG)
                .show();
    }

    public class ListActionCallBack{
        public void onListItemClick(Class item) {
            mPresenter.showItemDetail(item);
        }
    }



}
