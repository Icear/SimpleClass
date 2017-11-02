package com.github.Icear.NEFU.SimpleClass.ClassDetail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.github.Icear.NEFU.SimpleClass.Data.Entity.ClassInfo;
import com.github.Icear.NEFU.SimpleClass.R;
import com.github.Icear.NEFU.SimpleClass.SimpleClassApplication;
import com.github.Icear.NEFU.SimpleClass.Util.ClassUtil;
import com.github.Icear.Util.DateUtil;

import java.util.List;

/**
 * Created by icear on 2017/10/24.
 * 定制的ClassInfoRecyclerViewAdapter
 * 支持调用添加删除和交换item
 */

class ClassInfoRecyclerViewAdapter extends RecyclerView.Adapter<ClassInfoRecyclerViewAdapter.ViewHolder> {

    private List<ClassInfo> mValue;
    private ItemActionCallBack mItemActionCallBack;

    ClassInfoRecyclerViewAdapter(List<ClassInfo> items, ItemActionCallBack itemActionCallBack) {
        mValue = items;
        mItemActionCallBack = itemActionCallBack;
    }


    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(ViewHolder, int)} . Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(ViewHolder, int)
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_classinfo, parent, false);
        return new ViewHolder(itemView);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link ViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p>
     * Override {@link #onBindViewHolder(RecyclerView.ViewHolder, int)} instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValue.get(position);
        holder.mTextViewWeeks.setText(
                SimpleClassApplication.getApplication()
                        .getString(R.string.weekCount,
                                ClassUtil.toString(mValue.get(position).getWeek())));
        holder.mTextViewTime.setText(DateUtil.formatWeek(mValue.get(position).getWeekDay()) + " " +
                SimpleClassApplication.getApplication().getString(R.string.classSection, mValue.get(position).getSection()));
        holder.mTextViewLocation.setText(
                mValue.get(position).getLocation() + " " + mValue.get(position).getRoom());
        holder.mTextViewWeeks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemActionCallBack.onItemClick(holder.mItem);//TODO 修改为对应的三个事件处理器
            }
        });
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return mValue.size();
    }

    void delItem(int Position) {
        mValue.remove(Position);
    }

    interface ItemActionCallBack {
        void onItemClick(ClassInfo item);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ClassInfo mItem;
        private TextView mTextViewWeeks;
        private TextView mTextViewLocation;
        private TextView mTextViewTime;


        ViewHolder(View view) {
            super(view);
            mTextViewWeeks = (TextView) view.findViewById(R.id.textView_weeks);
            mTextViewLocation = (TextView) view.findViewById(R.id.textView_location);
            mTextViewTime = (TextView) view.findViewById(R.id.textView_time);
        }

    }
}
