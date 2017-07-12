package com.daily.news.subscription.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daily.news.subscription.OnItemClickListener;
import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;
import com.daily.news.subscription.mock.MockResponse;
import com.daily.news.subscription.model.Focus;
import com.daily.news.subscription.model.Recommend;
import com.daily.news.subscription.model.SubRecommend;
import com.daily.news.subscription.ui.adapter.RecommendAdapter;
import com.daily.news.subscription.ui.adapter.HeaderRecommendAdapter;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class RecommendFragment extends Fragment implements RecommendAdapter.OnSubscribeListener, OnItemClickListener<Recommend> {

    @BindView(R2.id.recommend_progress_container)
    View mProgressContainer;
    @BindView(R2.id.recommend_recyclerView)
    RecyclerView mRecyclerView;

    private View mFocusView;
    private Banner mFocusBanner;

    private List<Recommend> mRecommends = new ArrayList<>();
    private RecommendAdapter mRecommendAdapter;

    private HeaderRecommendAdapter mHeaderRecommendAdapter;


    public RecommendFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Observable<List<Focus>> focusObservable = Observable.timer(1000, TimeUnit.MICROSECONDS).flatMap(new Function<Long, ObservableSource<List<Focus>>>() {
            @Override
            public ObservableSource<List<Focus>> apply(@NonNull Long aLong) throws Exception {
                return Observable.just(MockResponse.getInstance().getFocusResponse());
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
        Observable<List<Recommend>> recommendObservable = Observable.timer(1000, TimeUnit.MILLISECONDS).flatMap(new Function<Long, ObservableSource<List<Recommend>>>() {
            @Override
            public ObservableSource<List<Recommend>> apply(@NonNull Long aLong) throws Exception {
                return Observable.just(MockResponse.getInstance().getRecommedResponse());
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());

        Observable.zip(focusObservable, recommendObservable, new BiFunction<List<Focus>, List<Recommend>, SubRecommend>() {
            @Override
            public SubRecommend apply(@NonNull List<Focus> focuses, @NonNull List<Recommend> recommends) throws Exception {
                SubRecommend subRecommend = new SubRecommend();
                subRecommend.focuses = focuses;
                subRecommend.recommends = recommends;
                return subRecommend;
            }
        }).subscribe(new Consumer<SubRecommend>() {
            @Override
            public void accept(@NonNull SubRecommend subRecommend) throws Exception {
                mProgressContainer.setVisibility(View.GONE);

                mFocusBanner.setImages(subRecommend.focuses);
                List<String> title = new ArrayList<>();
                for (int i = 0; i < subRecommend.focuses.size(); i++) {
                    title.add(subRecommend.focuses.get(i).docTitle);
                }
                mFocusBanner.setBannerTitles(title);
                mRecommends.addAll(subRecommend.recommends);
                mHeaderRecommendAdapter.notifyDataSetChanged();
                mFocusBanner.start();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recommend, container, false);
        ButterKnife.bind(this, root);
        mProgressContainer.setVisibility(View.VISIBLE);
        mHeaderRecommendAdapter = new HeaderRecommendAdapter();

        initRecommend();
        initFocusView(inflater, container);
        initRecommendHeader(inflater, container);

        return root;
    }

    /**
     * 初始化推荐相关
     */
    private void initRecommend() {
        mRecommendAdapter = new RecommendAdapter(getActivity(), mRecommends);
        mRecommendAdapter.setOnSubscribeListener(this);
        mRecommendAdapter.setOnItemClickListener(this);

        mHeaderRecommendAdapter.setRecommendAdapter(mRecommendAdapter);
        mRecyclerView.setAdapter(mHeaderRecommendAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    /**
     * 初始化顶部图库
     *
     * @param inflater
     * @param container
     */
    private void initFocusView(LayoutInflater inflater, ViewGroup container) {
        mFocusView = inflater.inflate(R.layout.item_focus, container, false);
        mFocusBanner = (Banner) mFocusView.findViewById(R.id.focus_banner);
        mFocusBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        mFocusBanner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                Glide.with(context).load(((Focus) path).picUrl).into(imageView);
            }
        });
        mFocusBanner.isAutoPlay(true);
        mFocusBanner.setIndicatorGravity(BannerConfig.RIGHT);
        mHeaderRecommendAdapter.addHeaderView(mFocusView);
    }

    /**
     * 初始化"大家都在看"栏目
     *
     * @param inflater
     * @param container
     */
    private void initRecommendHeader(LayoutInflater inflater, ViewGroup container) {
        View recommendHeaderView = inflater.inflate(R.layout.recommend_header, container, false);
        recommendHeaderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mHeaderRecommendAdapter.addHeaderView(recommendHeaderView);
    }

    /**
     * 点击订阅按钮
     *
     * @param recommend
     */
    @Override
    public void onSubscribe(Recommend recommend) {
        Toast.makeText(getActivity(), recommend.picUrl, Toast.LENGTH_SHORT).show();
    }

    /**
     * 点击item
     *
     * @param position  点击item的位置
     * @param recommend
     */
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
        if (mFocusBanner != null) {
            mFocusBanner.stopAutoPlay();
        }
    }
}
