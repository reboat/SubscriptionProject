package com.daily.news.subscription.rank;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.zjrb.core.recycleView.BaseRecyclerViewHolder;
import com.zjrb.core.recycleView.adapter.BaseRecyclerAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RankAdapter extends BaseRecyclerAdapter<RankResponse> {


    public RankAdapter(List<RankResponse> data) {
        super(data);
        addData(data, true);
    }


    @Override
    public BaseRecyclerViewHolder onAbsCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.subscription_item_rank, parent, false);
        return new RankViewHolder(itemView);
    }

    protected static class RankViewHolder extends BaseRecyclerViewHolder<RankResponse> {
        @BindView(R2.id.rank)
        TextView rank;
        @BindView(R2.id.column_imageView)
        ImageView columnImageView;
        @BindView(R2.id.column_title_view)
        TextView columnTitleView;
        @BindView(R2.id.column_info_view)
        TextView columnInfoView;
        @BindView(R2.id.hot)
        TextView hot;
        @BindView(R2.id.cnange)
        ImageView cnange;
        public RankViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bindView() {
            rank.setText(String.valueOf(getAdapterPosition() + 1));
            if(getAdapterPosition() == 0){
                rank.setTextColor(rank.getContext().getResources().getColor(R.color._d12324));
            }else if(getAdapterPosition() == 1){
                rank.setTextColor(rank.getContext().getResources().getColor(R.color._f0bd31));
            }
            else if(getAdapterPosition() == 2){
                rank.setTextColor(rank.getContext().getResources().getColor(R.color._666666));
            }else{
                rank.setTextColor(rank.getContext().getResources().getColor(R.color._bfbfbf));
            }

        }
    }
}
