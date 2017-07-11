package com.daily.news.subscription.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daily.news.subscription.OnItemClickListener;
import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.daily.news.subscription.model.Recommend;
import com.daily.news.subscription.ui.adapter.RecommendAdapter;
import com.daily.news.subscription.ui.adapter.SubscriptionAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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

public class RecommendFragment extends Fragment implements RecommendAdapter.OnSubscribeListener ,OnItemClickListener<Recommend>{

    @BindView(R2.id.recommend_progress_container)
    View mProgressContainer;
    @BindView(R2.id.recommend_recyclerView)
    RecyclerView mRecyclerView;

    private List<Recommend> mRecommends = new ArrayList<>();
    private RecommendAdapter mRecommendAdapter;

    private SubscriptionAdapter mSubscriptionAdapter;
    private TextView textView;

    public RecommendFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Observable.timer(1000, TimeUnit.MILLISECONDS)
                .flatMap(new Function<Long, ObservableSource<List<Recommend>>>() {
                    @Override
                    public ObservableSource<List<Recommend>> apply(@NonNull Long aLong) throws Exception {

                        List<Recommend> recommends = new ArrayList<>();
                        String[] names = {"发", "浙", "报", "人", "现", "锄", "额", "和"};
                        String[] imags = {
                                "http://img3.imgtn.bdimg.com/it/u=826828499,2412343960&fm=26&gp=0.jpg",
                                "http://img2.imgtn.bdimg.com/it/u=3732184319,1857452749&fm=26&gp=0.jpg",
                                "http://img0.imgtn.bdimg.com/it/u=2767677514,459923336&fm=26&gp=0.jpg",
                                "http://img1.imgtn.bdimg.com/it/u=638461789,1813123122&fm=26&gp=0.jpg",
                                "http://img3.imgtn.bdimg.com/it/u=1679448770,3520010627&fm=26&gp=0.jpg"
                        };
                        Random random = new Random();
                        for (int i = 0; i < 30; i++) {
                            Recommend recommend = new Recommend();
                            recommend.articleCount = random.nextInt();
                            for (int j = 0; j < 4; j++) {
                                recommend.name += names[random.nextInt(names.length)];
                            }

                            recommend.subscribeCount = random.nextInt();
                            recommend.picUrl = imags[random.nextInt(imags.length)];
                            recommends.add(recommend);
                        }
                        return Observable.just(recommends);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Recommend>>() {
                    @Override
                    public void accept(@NonNull List<Recommend> recommends) throws Exception {
                        mProgressContainer.setVisibility(View.GONE);
                        mRecommends.clear();
                        mRecommends.addAll(recommends);

                        textView = new TextView(getActivity());
                        textView.setTextSize(30);
                        textView.setText("我是头部1");
                        TextView textView2 = new TextView(getActivity());
                        textView2.setTextSize(30);
                        textView2.setText("我是头部2");
                        mSubscriptionAdapter.addHeaderView(textView);
                        mSubscriptionAdapter.addHeaderView(textView2);

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
