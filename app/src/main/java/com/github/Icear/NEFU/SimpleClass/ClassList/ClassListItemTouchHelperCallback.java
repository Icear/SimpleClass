package com.github.Icear.NEFU.SimpleClass.ClassList;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import static android.support.v7.widget.helper.ItemTouchHelper.DOWN;
import static android.support.v7.widget.helper.ItemTouchHelper.RIGHT;
import static android.support.v7.widget.helper.ItemTouchHelper.UP;

/**
 * Created by icear on 2017/10/23.
 * 自定义的ItemTouchHelper，实现了右滑删除以及长按上下拖拽的功能
 */

class ClassListItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private RecyclerView mRecyclerView;

    ClassListItemTouchHelperCallback(RecyclerView recyclerView) {
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
            ClassListRecyclerViewAdapter adapter = (ClassListRecyclerViewAdapter) mRecyclerView.getAdapter();
            adapter.delItem(viewHolder.getAdapterPosition());
        }
    }
}
