package com.github.Icear.NEFU.SimpleClass.ClassList;


import android.content.res.ColorStateList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.Icear.NEFU.SimpleClass.Data.Class.Class;
import com.github.Icear.NEFU.SimpleClass.R;
import com.github.Icear.NEFU.SimpleClass.Util.RandomColorUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Class} and makes a call to the
 * ListActionCallBack
 */
public class ClassListRecyclerViewAdapter extends RecyclerView.Adapter<ClassListRecyclerViewAdapter.ViewHolder> {

    private final List<Class> mItemList;
    private final List<Integer> mColorList;
    private final ClassListFragment.ListActionCallBack mListener;

    public ClassListRecyclerViewAdapter(List<Class> items, ClassListFragment.ListActionCallBack listener) {
        //在这里创建Adapter并传入要展示的item数据，同时设定传到上层的单击监听事件
        mItemList = items;
        mListener = listener;
        mColorList = new ArrayList<>();
        for(int i = 0; i < items.size(); i++){
            mColorList.add(RandomColorUtil.getRandomColor());//生成随机的颜色
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //在这里创建view并放入viewHolder
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_class, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //在这里给View填充(覆盖)内容
        holder.mItem = mItemList.get(position);
        holder.mTitle.setText(holder.mItem.getName());
        holder.mSubtitle.setText(holder.mItem.getTeachers());
        holder.mIcon.setBackgroundTintList(ColorStateList.valueOf(mColorList.get(position)));//从ColorList中取出对应的颜色并填充
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListItemClick(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitle;
        public final TextView mSubtitle;
        public final ImageView mIcon;
        public Class mItem;
        public ViewHolder(View view) {
            //在这里预先hold住View中的元素并保存到holder中
            super(view);
            mView = view;
            mIcon = (ImageView) view.findViewById(R.id.imageView_class);
            mTitle = (TextView) view.findViewById(R.id.textView_title);
            mSubtitle = (TextView) view.findViewById(R.id.textView_subTitle);
        }
//        @Override
//        public String toString() {
//            return super.toString() + " '" + mSubtitle.getText() + "'";
//        }
    }

    /**
     * 向数据表添加指定item
     * @param item item
     */
    public void addItem(Class item){
        if(item != null){
            mItemList.add(item);
            mColorList.add(RandomColorUtil.getRandomColor());//同时为新的item添加颜色
            notifyDataSetChanged();
        }else{
            throw new NullPointerException("the item is null");
        }
    }

    /**
     * 从数据表删除指定item，如果item不存在则操作无效
     * @param position position
     */
    public void delItem(int position){
        if(0 < position && position < mItemList.size()){
            mItemList.remove(position);
            mColorList.remove(position);//同时移除颜色列表的对应值
            notifyDataSetChanged();
        }else{
            throw new IndexOutOfBoundsException("position: " + position);
        }
    }

    /**
     * 交换数据表中指定两个item的位置，如果item不存在则操作无效
     * @param position1 position
     * @param position2 position
     */
    public void swapItem(int position1, int position2){
        if(0 < position1 && 0 < position2
                && position1 < mItemList.size() && position2 <mItemList.size()){
            Collections.swap(mItemList,position1,position2);
            Collections.swap(mColorList,position1,position2);
            notifyDataSetChanged();
        }else{
            throw new IndexOutOfBoundsException("position1: " + position1 + " position2:" + position2);
        }
    }

}
