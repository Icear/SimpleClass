package indi.github.icear.simpleclass.calendarimport;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import indi.github.icear.simpleclass.R;
import indi.github.icear.simpleclass.SimpleClassApplication;
import indi.github.icear.simpleclass.data.academicdata.entity.Class;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Class} and makes a call to the
 * ListActionCallBack
 */
class CalendarImportRecyclerViewAdapter extends RecyclerView.Adapter<CalendarImportRecyclerViewAdapter.ViewHolder> {

    private final List<Class> mItemList;
    private final List<Boolean> mStatusList;

    CalendarImportRecyclerViewAdapter(List<Class> items, List<Boolean> statusList) {
        //在这里创建Adapter并传入要展示的item数据，同时设定传到上层的单击监听事件
        mItemList = items;
        mStatusList = statusList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //在这里创建view并放入viewHolder
        View view = LayoutInflater.from(parent.getContext())
                .inflate(indi.github.icear.simpleclass.R.layout.item_class, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //在这里给View填充(覆盖)内容
        holder.mItem = mItemList.get(position);
        holder.mTitle.setText(mItemList.get(position).getName());
        holder.mSubtitle.setText(mItemList.get(position).getTeachers());
        if (mStatusList.get(position)) {
            //状态为成功
            holder.mIcon.setImageResource(indi.github.icear.simpleclass.R.drawable.ic_check_circle_black_24dp);
            holder.mIcon.setColorFilter(SimpleClassApplication.getApplication().getResources().getColor(R.color.green));
        } else {
            //状态为失败
            holder.mIcon.setImageResource(indi.github.icear.simpleclass.R.drawable.ic_error_black_24dp);
            holder.mIcon.setColorFilter(SimpleClassApplication.getApplication().getResources().getColor(R.color.yellow));
        }

    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mTitle;
        final TextView mSubtitle;
        final ImageView mIcon;
        Class mItem;

        ViewHolder(View view) {
            //在这里预先hold住View中的元素并保存到holder中
            super(view);
            mView = view;
            mIcon = (ImageView) view.findViewById(indi.github.icear.simpleclass.R.id.imageView_class);
            mTitle = (TextView) view.findViewById(indi.github.icear.simpleclass.R.id.textView_title);
            mSubtitle = (TextView) view.findViewById(indi.github.icear.simpleclass.R.id.textView_subTitle);
        }
//        @Override
//        public String toString() {
//            return super.toString() + " '" + mSubtitle.getText() + "'";
//        }
    }
}
