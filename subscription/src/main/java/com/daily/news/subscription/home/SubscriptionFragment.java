package com.daily.news.subscription.home;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.daily.news.subscription.article.ArticleAdapter;
import com.daily.news.subscription.article.ArticleResponse;
import com.daily.news.subscription.base.HeaderAdapter;
import com.daily.news.subscription.base.LinearLayoutColorDivider;
import com.daily.news.subscription.base.OnItemClickListener;
import com.daily.news.subscription.more.column.ColumnAdapter;
import com.daily.news.subscription.more.column.ColumnResponse;
import com.idisfkj.loopview.LoopView;
import com.idisfkj.loopview.entity.LoopViewEntity;
import com.zjrb.core.api.base.APIGetTask;
import com.zjrb.core.api.callback.LoadingCallBack;
import com.zjrb.core.common.base.page.LoadMore;
import com.zjrb.core.common.listener.LoadMoreListener;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.ui.holder.FooterLoadMore;
import com.zjrb.core.ui.holder.HeaderRefresh;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * 页面逻辑：
 * 1.有订阅时返回订阅的新闻，无订阅时返回推荐订阅栏目。
 * 2.点击订阅后页面下拉刷新，返回订阅栏目的新闻
 */
public class SubscriptionFragment extends Fragment implements SubscriptionContract.View {

    @BindView(R2.id.subscription_recyclerView)
    RecyclerView mRecyclerView;
    HeaderAdapter mHeaderAdapter;

    private SubscriptionContract.Presenter mPresenter;
    private HeaderRefresh mRefreshView;
    private Unbinder mUnBinder;
    private FooterLoadMore<ArticleResponse.DataBean> mFooterLoadMore;

    public SubscriptionFragment() {
    }

    public static SubscriptionFragment newInstance() {
        SubscriptionFragment subscriptionFragment = new SubscriptionFragment();
        new SubscriptionPresenter(subscriptionFragment, new SubscriptionStore());
        return subscriptionFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.subscribe("杭州");
    }

    @Override
    public void setPresenter(SubscriptionContract.Presenter presenter) {
        mPresenter = presenter;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_subscription_home, container, false);
        mUnBinder = ButterKnife.bind(this, rootView);
        setupRecyclerView();
        return rootView;
    }

    /**
     * 初始化RecyclerView
     */
    private void setupRecyclerView() {
        mHeaderAdapter = new HeaderAdapter();
        mRecyclerView.setAdapter(mHeaderAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new LinearLayoutColorDivider(getResources(), R.color.dddddd, R.dimen.divide_height, LinearLayoutManager.VERTICAL));

        mRefreshView = new HeaderRefresh(mRecyclerView);
        mRefreshView.setOnRefreshListener(new HeaderRefresh.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.onRefresh("杭州");
            }
        });
        mHeaderAdapter.addHeaderView(mRefreshView.getItemView());
    }

    @Override
    public void hideProgressBar() {
        mRefreshView.setRefreshing(false);
    }

    @Override
    public void showProgressBar() {
        mRefreshView.setRefreshing(true);
    }

    @Override
    public void showError(String message) {
        mRefreshView.setRefreshing(false);
    }

    @Override
    public void onRefreshComplete(SubscriptionResponse.DataBean data) {
        mRefreshView.setRefreshing(false);
        //RecycleView会缓存ViewHolder，Adapter中的数据结构发生变化，但缓存的ViewHolder没有变化导致crash。重新设置Adapter清除缓存。
        mRecyclerView.setAdapter(mHeaderAdapter);
        updateValue(data);
    }

    @Override
    public void onRefreshError(String message) {
        mRefreshView.setRefreshing(false);
    }

    /**
     * 网络请求返回时回调
     *
     * @param data
     */
    @Override
    public void updateValue(SubscriptionResponse.DataBean data) {

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        if (data.has_subscribe) {
            ArticleAdapter subscriptionAdapter = createMySubscriptionAdapter(data, inflater);
            mRecyclerView.setAdapter(subscriptionAdapter);
        } else if (!data.has_subscribe) {
            HeaderAdapter recommendAdapter = createRecommendAdapter(data, inflater);
            mHeaderAdapter.setInternalAdapter(recommendAdapter);
        }

    }

    /**
     * 创建我的订阅Adapter
     *
     * @param subscriptionBean
     * @param inflater
     * @return
     */
    @NonNull
    private ArticleAdapter createMySubscriptionAdapter(final SubscriptionResponse.DataBean subscriptionBean, LayoutInflater inflater) {
        View headerView = setupMySubscriptionHeaderView(inflater, (ViewGroup) getView());
        final List<ArticleResponse.DataBean.Article> articles = subscriptionBean.article_list;
        final ArticleAdapter articleAdapter = new ArticleAdapter(articles);
        articleAdapter.addHeaderView(mRefreshView.getItemView());
        articleAdapter.addHeaderView(headerView);

         mFooterLoadMore = new FooterLoadMore<>(mRecyclerView, new LoadMoreListener<ArticleResponse.DataBean>() {
            @Override
            public void onLoadMoreSuccess(ArticleResponse.DataBean data, LoadMore loadMore) {
                articles.addAll(data.elements);
                articleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLoadMore(LoadingCallBack<ArticleResponse.DataBean> callback) {
                APIGetTask task = new APIGetTask<ArticleResponse.DataBean>(callback) {
                    @Override
                    protected void onSetupParams(Object... params) {
                        put("start", params[0]);
                        put("size", params[1]);
                    }

                    @Override
                    protected String getApi() {
                        return "/api/column/my_article_list";
                    }
                };

                if(articles!=null && articles.size()>0){
                    task.exe(articles.get(articles.size() - 1).sort_number, 10);
                }else{
                    mFooterLoadMore.setState(LoadMore.TYPE_NO_MORE);
                }
            }
        });
        articleAdapter.addFooterView(mFooterLoadMore.getItemView());
        return articleAdapter;
    }

    /**
     * 无订阅时创建推荐Adapter
     *
     * @param subscriptionBean
     * @param inflater
     * @return
     */
    @NonNull
    private HeaderAdapter createRecommendAdapter(SubscriptionResponse.DataBean subscriptionBean, LayoutInflater inflater) {
        HeaderAdapter adapter = new HeaderAdapter();

        final View bannerView = setupBannerView(inflater, (ViewGroup) getView(), subscriptionBean.focus_list);
        if (bannerView != null) {
            adapter.addHeaderView(bannerView);
        }

        View moreSubscriptionView = setupMoreSubscriptionView(inflater, (ViewGroup) getView());
        adapter.addHeaderView(moreSubscriptionView);
//
        ColumnAdapter columnAdapter = new ColumnAdapter(subscriptionBean.recommend_list);
        columnAdapter.setOnItemClickListener(new OnItemClickListener<ColumnResponse.DataBean.ColumnBean>() {
            @Override
            public void onItemClick(int position, ColumnResponse.DataBean.ColumnBean item) {
                Nav.with(getActivity())
                        .to(Uri.parse("http://www.8531.cn/subscription/detail").buildUpon().appendQueryParameter("id", String.valueOf(item.id))
                        .build(), 0);
            }
        });
        columnAdapter.setOnSubscribeListener(new ColumnAdapter.OnSubscribeListener() {
            @Override
            public void onSubscribe(ColumnResponse.DataBean.ColumnBean bean) {
                mPresenter.submitSubscribe(bean);
                bean.subscribed = !bean.subscribed;
                mHeaderAdapter.notifyDataSetChanged();
            }
        });
        adapter.setInternalAdapter(columnAdapter);
        return adapter;
    }

    @Override
    public void subscribeSuc(ColumnResponse.DataBean.ColumnBean bean) {

    }

    @Override
    public void subscribeFail(ColumnResponse.DataBean.ColumnBean bean, String message) {
        bean.subscribed = !bean.subscribed;
        mHeaderAdapter.notifyDataSetChanged();
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 有订阅时，订阅栏目上的导航
     *
     * @param inflater
     * @param container
     * @return
     */
    @NonNull
    private View setupMySubscriptionHeaderView(LayoutInflater inflater, ViewGroup container) {
        View headerView = inflater.inflate(R.layout.header_my_subscription, container, false);

        headerView.findViewById(R.id.my_sub_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Nav.with(v.getContext()).to("http://www.8531.cn/subscription/more/my/column");
            }
        });

        headerView.findViewById(R.id.my_sub_more_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Nav.with(v.getContext()).to("http://www.8531.cn/subscription/more");
            }
        });
        return headerView;
    }


    /**
     * 无订阅时，推荐上的轮播图
     *
     * @param inflater
     * @param container
     * @param focuses
     * @return
     */
    @NonNull
    private View setupBannerView(LayoutInflater inflater, ViewGroup container, List<SubscriptionResponse.Focus> focuses) {
        if (focuses == null || focuses.size() == 0) {
            return null;
        }
        final LoopView loopView = (LoopView) inflater.inflate(R.layout.item_focus, container, false);
        Observable.fromIterable(focuses).flatMap(new Function<SubscriptionResponse.Focus, ObservableSource<LoopViewEntity>>() {
            @Override
            public ObservableSource<LoopViewEntity> apply(@io.reactivex.annotations.NonNull SubscriptionResponse.Focus focus) throws Exception {
                LoopViewEntity entity = new LoopViewEntity();
                entity.setDescript(focus.doc_title);
                entity.setImageUrl(focus.pic_url);
                return Observable.just(entity);
            }
        }).toList().subscribe(new Consumer<List<LoopViewEntity>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull List<LoopViewEntity> loopViewEntities) throws Exception {
                loopView.setLoopData(loopViewEntities);
            }
        });
        return loopView;
    }

    /**
     * 无订阅页面，推荐上面的更多导航
     *
     * @param inflater
     * @param container
     * @return
     */
    @NonNull
    private View setupMoreSubscriptionView(LayoutInflater inflater, ViewGroup container) {
        View moreHeaderView = inflater.inflate(R.layout.header_more, container, false);
        moreHeaderView.findViewById(R.id.no_subscription_more_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Nav.with(v.getContext()).to("http://www.8531.cn/subscription/more");
            }
        });
        return moreHeaderView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnBinder.unbind();
    }
}
