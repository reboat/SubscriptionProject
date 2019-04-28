//package com.daily.news.subscription.sail;
//
//import android.content.Context;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Parcelable;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.annotation.RequiresApi;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v4.widget.NestedScrollView;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.ViewTreeObserver;
//import android.view.WindowManager;
//import android.widget.FrameLayout;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.daily.news.subscription.R;
//import com.daily.news.subscription.R2;
//import com.daily.news.subscription.home.RecommendFragment;
//import com.daily.news.subscription.home.SubscriptionResponse;
//import com.daily.news.subscription.more.column.ColumnPresenter;
//import com.daily.news.subscription.my.MyColumnFragment;
//import com.daily.news.subscription.my.MyStore;
//import com.daily.news.subscription.rank.RankFragment;
//import com.daily.news.subscription.view.DailyNestedScrollView;
//import com.trs.tasdk.entity.ObjectType;
//import com.zjrb.core.utils.click.ClickTracker;
//import com.zjrb.daily.db.bean.ChannelBean;
//import com.zjrb.daily.news.bean.FocusBean;
//import com.zjrb.daily.news.ui.holder.HeaderBannerHolder;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import cn.daily.news.biz.core.nav.Nav;
//
//public class SailFragment extends Fragment {
//
//
//    private static final String FOCUS_DATA = "focus_data";
//    @BindView(R2.id.sail_banner)
//    RelativeLayout sailBanner;
//    @BindView(R2.id.choice)
//    TextView choice;
//    @BindView(R2.id.subscribe)
//    TextView subscribe;
//    @BindView(R2.id.rank)
//    TextView rank;
//    @BindView(R2.id.more)
//    TextView more;
//    @BindView(R2.id.menu)
//    RelativeLayout menu;
//    @BindView(R2.id.framelayout)
//    FrameLayout framelayout;
//    @BindView(R2.id.scrollview)
//    DailyNestedScrollView scrollview;
//    @BindView(R2.id.top_choice)
//    TextView topChoice;
//    @BindView(R2.id.top_subscribe)
//    TextView topSubscribe;
//    @BindView(R2.id.top_rank)
//    TextView topRank;
//    @BindView(R2.id.top_more)
//    TextView topMore;
//    @BindView(R2.id.top_menu)
//    RelativeLayout topMenu;
//
//    int scrollHeight;
//    int menuHeight;
//
//    View bannerView;
//
//    Fragment   choiceFragment,subFragment, rankFragment;
//    private FragmentManager mFragmentManager;
//
//    public static Fragment newInstance(List<SubscriptionResponse.Focus> focus_list) {
//        SailFragment fragment = new SailFragment();
//        if (focus_list != null && focus_list.size() > 0) {
//            focus_list.removeAll(Collections.singleton(null));
//        }
//        Bundle args = new Bundle();
//        args.putParcelableArrayList(FOCUS_DATA, (ArrayList<? extends Parcelable>) focus_list);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.subscription_fragment_sail, container, false);
//        ButterKnife.bind(this, rootView);
//        bannerView = setupBannerView(getArguments().<SubscriptionResponse.Focus>getParcelableArrayList(FOCUS_DATA));
//        if(bannerView != null) {
//            sailBanner.addView(bannerView);
//        }
//
//        return rootView;
//    }
//
//    private void initFragment() {
//        mFragmentManager = getChildFragmentManager();
//        if(mFragmentManager.findFragmentByTag("choiceFragment") != null){
//
//        }else{
//            choiceFragment = RankFragment.instance(new ChannelBean(), true);
//            subFragment = RankFragment.instance(new ChannelBean(), true);
//
//            rankFragment = RankFragment.instance(new ChannelBean(), true);
//
//            mFragmentManager.beginTransaction().add(R.id.framelayout, choiceFragment, "choiceFragment").commit();
//            mFragmentManager.beginTransaction().add(R.id.framelayout, subFragment, "subFragment").commit();
//            mFragmentManager.beginTransaction().add(R.id.framelayout, rankFragment, "rankFragment").commit();
//        }
//
//
//
//
//        onViewClicked(choice);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        initView(view);
//        initFragment();
//
//    }
//
//    private void initView(View view) {
//
//        if(bannerView != null) {
//            WindowManager wm = (WindowManager) getContext()
//                    .getSystemService(Context.WINDOW_SERVICE);
//
//            int width = wm.getDefaultDisplay().getWidth();
//            scrollHeight = width * 178 / 360;
//        }
//
//        scrollview.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(NestedScrollView nestedScrollView, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                if (scrollY < scrollHeight) {
//                    topMenu.setVisibility(View.GONE);
//                } else if (scrollY >= scrollHeight) {
//                    topMenu.setVisibility(View.VISIBLE);
//                }
//            }
//        });
//        autoFitScreen();
//    }
//
//    /**
//     * 动态设置framelayout的高度，防止内部RecycleView加载全部数据
//     */
//    private void autoFitScreen(){
//        menuHeight = menu.getLayoutParams().height;
//        Log.e("menuHeight", menuHeight + "");
//        final View rootView = getActivity().findViewById(R.id.main);
//        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//            @Override
//            public void onGlobalLayout() {
//                rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                scrollview.setMyScrollHeight((int) menu.getY());
//
//                /**
//                 * setBehavior_Y（），传入外层behavior动画执行后折叠view的高度
//                 */
//                scrollview.setBehavior_Y(252);
//
//                int rvNewHeight = rootView.getHeight() - menuHeight;
//                /**
//                 * framelayout 在xml中height须设置一个相对小的固定值，如果设置MATCH_PARENT或WRAP_CONTENT则无法动态设置其高度
//                 *
//                 */
//                framelayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, rvNewHeight));
//            }
//
//        });
//    }
//
//
//    private View setupBannerView(final List<SubscriptionResponse.Focus> focuses) {
//        if (focuses == null || focuses.size() == 0) {
//            return null;
//        }
//        List<FocusBean> focusBeans = new ArrayList<>();
//        for (int i = 0; i < focuses.size(); i++) {
//            FocusBean bean = new FocusBean();
//            SubscriptionResponse.Focus focus = focuses.get(i);
//            bean.setId(focus.channel_article_id);
//            bean.setImage_url(focus.pic_url);
//            bean.setUrl(focus.doc_url);
//            bean.setSort_number(focus.sort_number);
//            bean.setTitle(focus.doc_title);
//            bean.setTag(focus.tag);
//            focusBeans.add(bean);
//        }
//        HeaderBannerHolder bannerHolder = new HeaderBannerHolder(sailBanner) {
//            @Override
//            public void onItemClick(View item, int position) {
//                if (ClickTracker.isDoubleClick()) {
//                    return;
//                }
//
//                SubscriptionResponse.Focus focus = focuses.get(position);
//                if (!TextUtils.isEmpty(focus.doc_url)) {
//                    Nav.with(item.getContext()).to(focus.doc_url);
//                }
//
//
//            }
//        };
//        bannerHolder.setData(focusBeans);
//        return bannerHolder.getItemView();
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//    }
//
//    @OnClick({R2.id.choice, R2.id.subscribe, R2.id.rank, R2.id.more, R2.id.top_choice, R2.id.top_subscribe, R2.id.top_rank, R2.id.top_more})
//    public void onViewClicked(View view) {
//        int id = view.getId();
//        if(id == R.id.choice || id == R.id.top_choice){
//            choice.setSelected(true);
//            topChoice.setSelected(true);
//            subscribe.setSelected(false);
//            topSubscribe.setSelected(false);
//            rank.setSelected(false);
//            topRank.setSelected(false);
//            switchFragment(choiceFragment, subFragment, rankFragment);
//
//        }
//        if(id == R.id.subscribe || id == R.id.top_subscribe){
//            choice.setSelected(false);
//            topChoice.setSelected(false);
//            subscribe.setSelected(true);
//            topSubscribe.setSelected(true);
//            rank.setSelected(false);
//            topRank.setSelected(false);
//            switchFragment(subFragment, choiceFragment, rankFragment);
//        }
//
//        if(id == R.id.rank || id == R.id.top_rank){
//            choice.setSelected(false);
//            topChoice.setSelected(false);
//            rank.setSelected(true);
//            topRank.setSelected(true);
//            subscribe.setSelected(false);
//            topSubscribe.setSelected(false);
//            switchFragment(rankFragment, choiceFragment, subFragment);
//        }
//
//        if(id == R.id.more || id == R.id.top_more){
//            Nav.with(this).toPath("/subscription/more");
//        }
//
//
//    }
//
//    private void switchFragment(Fragment show, Fragment hide1, Fragment hide2) {
//        FragmentTransaction transaction = mFragmentManager.beginTransaction();
//        transaction.show(show);
//        if (hide1 != null) {
//            transaction.hide(hide1);
//        }
//        if (hide2 != null) {
//            transaction.hide(hide2);
//        }
//        transaction.commitAllowingStateLoss();
//    }
//}
