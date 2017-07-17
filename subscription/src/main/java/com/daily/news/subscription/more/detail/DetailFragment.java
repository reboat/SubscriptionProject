package com.daily.news.subscription.more.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daily.news.subscription.R;
import com.daily.news.subscription.more.CategoryContent;

public class DetailFragment extends Fragment implements DetailContract.View {
    public static final String ARG_ITEM_ID = "item_id";
    private CategoryContent.CategoryItem mItem;
    private DetailContract.Presenter mPresenter;

    public DetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mItem = CategoryContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.subscribe();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.more_detail, container, false);

        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.more_detail)).setText(mItem.details);
        }

        return rootView;
    }

    @Override
    public void setPresenter(DetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void updateValue(CategoryContent.CategoryItem categoryItem) {

        mItem=categoryItem;

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }
}
