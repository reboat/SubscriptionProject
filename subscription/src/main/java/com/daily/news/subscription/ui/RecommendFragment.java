package com.daily.news.subscription.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daily.news.subscription.OnItemClickListener;
import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.daily.news.subscription.mock.MockResponse;
import com.daily.news.subscription.model.Recommend;
import com.daily.news.subscription.ui.adapter.RecommendAdapter;
import com.daily.news.subscription.ui.adapter.SubscriptionAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class RecommendFragment extends Fragment implements RecommendAdapter.OnSubscribeListener, OnItemClickListener<Recommend> {

    @BindView(R2.id.recommend_progress_container)
    View mProgressContainer;
    @BindView(R2.id.recommend_recyclerView)
    RecyclerView mRecyclerView;

    private List<Recommend> mRecommends = new ArrayList<>();
    private RecommendAdapter mRecommendAdapter;

    private SubscriptionAdapter mSubscriptionAdapter;

    public RecommendFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Observable.timer(1000, TimeUnit.MILLISECONDS)
                .flatMap(new Function<Long, ObservableSource<List<Recommend>>>() {
                    @Override
                    public ObservableSource<List<Recommend>> apply(@NonNull Long aLong) throws Exception {
                        return Observable.just(MockResponse.getInstance().getRecommedResponse());
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Recommend>>() {
                    @Override
                    public void accept(@NonNull List<Recommend> recommends) throws Exception {
                        mProgressContainer.setVisibility(View.GONE);
                        mRecommends.addAll(recommends);
                        mSubscriptionAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recommend, container, false);
        ButterKnife.bind(this, root);
        mProgressContainer.setVisibility(View.VISIBLE);

        mRecommendAdapter = new RecommendAdapter(getActivity(), mRecommends);
        mRecommendAdapter.setOnSubscribeListener(this);
        mRecommendAdapter.setOnItemClickListener(this);

        mSubscriptionAdapter = new SubscriptionAdapter();
        mSubscriptionAdapter.setRecommendAdapter(mRecommendAdapter);
        mRecyclerView.setAdapter(mSubscriptionAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return root;
    }

    @Override
    public void onSubscribe(Recommend recommend) {
        Toast.makeText(getActivity(), recommend.picUrl, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(int position, Recommend recommend) {
        Toast.makeText(getActivity(), recommend.name, Toast.LENGTH_SHORT).show();
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
