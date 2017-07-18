package com.daily.news.subscription.detail;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.daily.news.subscription.more.column.Column;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailFragment extends Fragment implements DetailContract.View{
    private static final String UID = "uid";

    private String mUid;

    private DetailContract.Presenter mPresenter;

    @BindView(R2.id.detail_articles)
    RecyclerView mRecyclerView;


    public DetailFragment() {
    }

    public static DetailFragment newInstance(String uid) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(UID, uid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUid = getArguments().getString(UID);
        }
        Toast.makeText(getActivity(), "UID:" + mUid, Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, rootView);

        mRecyclerView.setAdapter(new DetailArticleAdapter());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.subscribe();
    }

    @Override
    public void setPresenter(DetailContract.Presenter presenter) {
        mPresenter=presenter;
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void updateValue(List<Column> columnsBeen) {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
