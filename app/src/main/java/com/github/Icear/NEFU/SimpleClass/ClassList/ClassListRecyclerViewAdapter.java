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
import java.util.List;
import java.util.Random;

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
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //在这里创建view并放入viewHolder
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_class, parent, false);
        mColorList.add(RandomColorUtil.getRandomColor());//生成对应view时给它生成随机的颜色并记录
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //在这里给View填充内容
        holder.mItem = mItemList.get(position);
        holder.mTitle.setText(mItemList.get(position).getName());
        holder.mSubtitle.setText(mItemList.get(position).getTeachers());
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
}
