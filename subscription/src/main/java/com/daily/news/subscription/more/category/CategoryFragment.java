package com.daily.news.subscription.more.category;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.daily.news.subscription.more.MoreContract;
import com.daily.news.subscription.more.MoreDetailFragment;
import com.daily.news.subscription.more.CategoryContent;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryFragment extends Fragment implements MoreContract.View {

    private MoreContract.Presenter mPresenter;

    @BindView(R2.id.more_category_list)
    RecyclerView recyclerView;

    public CategoryFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_category, container, false);
        ButterKnife.bind(this, rootView);
        setupRecyclerView(recyclerView);


        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        mPresenter.subscribe();
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new CategoryFragment.SimpleItemRecyclerViewAdapter(CategoryContent.ITEMS));
    }

    @Override
    public void setPresenter(MoreContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<CategoryFragment.SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<CategoryContent.CategoryItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<CategoryContent.CategoryItem> items) {
            mValues = items;
        }

        @Override
        public CategoryFragment.SimpleItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.more_list_content, parent, false);
            return new CategoryFragment.SimpleItemRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final CategoryFragment.SimpleItemRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).id);
            holder.mContentView.setText(mValues.get(position).content);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle arguments = new Bundle();
                    arguments.putString(MoreDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                    MoreDetailFragment fragment = new MoreDetailFragment();
                    fragment.setArguments(arguments);
                    getChildFragmentManager().beginTransaction()
                            .replace(R.id.more_category_detail_container, fragment)
                            .commit();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public CategoryContent.CategoryItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
}

