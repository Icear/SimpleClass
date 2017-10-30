package com.github.Icear.NEFU.SimpleClass.ClassDetail;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.github.Icear.NEFU.SimpleClass.ClassList.ClassListRecyclerViewAdapter;

import static android.support.v7.widget.helper.ItemTouchHelper.DOWN;
import static android.support.v7.widget.helper.ItemTouchHelper.RIGHT;
import static android.support.v7.widget.helper.ItemTouchHelper.UP;

/**
 * Created by icear on 2017/10/30.
 * 自定义的ItemTouchHelper，实现了右滑删除,与上下拖拽
 */

class ClassInfoItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private RecyclerView mRecyclerView;

    ClassInfoItemTouchHelperCallback(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(UP | DOWN, RIGHT);//允许上下拖拽，允许右滑
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        ClassListRecyclerViewAdapter adapter = (ClassListRecyclerViewAdapter) recyclerView.getAdapter();
        adapter.swapItem(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (direction == RIGHT) {
            //在这里选择删除
            ClassInfoRecyclerViewAdapter adapter = (ClassInfoRecyclerViewAdapter) mRecyclerView.getAdapter();
            adapter.delItem(viewHolder.getAdapterPosition());
        }
    }
}
