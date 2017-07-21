package com.daily.news.subscription.more.column;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.daily.news.subscription.OnItemClickListener;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lixinke on 2017/7/17.
 */

public class ColumnAdapter extends RecyclerView.Adapter<ColumnAdapter.ColumnViewHolder> {
    private List<Column> mColumnsBeens;
    private OnSubscribeListener mOnSubscribeListener;


    private OnItemClickListener mOnItemClickListener;

    public ColumnAdapter(List<Column> columnsBeens) {
        mColumnsBeens = columnsBeens;
    }

    public void updateValues(List<Column> columnsBeens) {
        mColumnsBeens.clear();
        mColumnsBeens.addAll(columnsBeens);
//        notifyDataSetChanged();
    }

    public void addMoreValues(List<Column> columnsBeens) {
        mColumnsBeens.addAll(columnsBeens);
    }

    @Override
    public ColumnViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_column, parent, false);
        return new ColumnViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ColumnViewHolder holder, final int position) {
        final Column column = mColumnsBeens.get(position);
        holder.mTitleView.setText(column.name);
        holder.mColumnInfosView.setText(String.format(Locale.getDefault(),holder.itemView.getContext().getString(R.string.column_info_format), column.subscribe_count, column.article_count));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(position, column);
                }
            }
        });
        String subscriptionText=column.subscribed?holder.itemView.getContext().getString(R.string.has_been_subscribed):holder.itemView.getContext().getString(R.string.subscription);
        holder.mSubscribeBtn.setText(subscriptionText);
        holder.mSubscribeBtn.setSelected(column.subscribed);
        holder.mSubscribeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnSubscribeListener != null) {
                    mOnSubscribeListener.onSubscribe(column);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mColumnsBeens != null ? mColumnsBeens.size() : 0;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnSubscribeListener(OnSubscribeListener onSubscribeListener) {
        mOnSubscribeListener = onSubscribeListener;
    }

    /**
     * 点击和订阅回调
     */
    public interface OnSubscribeListener {
        void onSubscribe(Column bean);
    }

    protected static class ColumnViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.column_imageView)
        ImageView mImageView;
        @BindView(R2.id.column_title_view)
        TextView mTitleView;
        @BindView(R2.id.column_infos_view)
        TextView mColumnInfosView;
        @BindView(R2.id.column_subscribe_btn)
        TextView mSubscribeBtn;

        public ColumnViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
