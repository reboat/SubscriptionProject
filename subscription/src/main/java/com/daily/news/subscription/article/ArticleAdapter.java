package com.daily.news.subscription.article;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.zjrb.core.common.base.BaseRecyclerViewHolder;
import com.zjrb.daily.news.bean.ArticleItemBean;
import com.zjrb.daily.news.bean.ColumnWidget;
import com.zjrb.daily.news.bean.type.DocType;
import com.zjrb.daily.news.bean.type.ListStyle;
import com.zjrb.daily.news.ui.adapter.NewsBaseAdapter;
import com.zjrb.daily.news.ui.holder.NewsActivityHolder;
import com.zjrb.daily.news.ui.holder.NewsColumnHolder;
import com.zjrb.daily.news.ui.holder.NewsLargeImageHolder;
import com.zjrb.daily.news.ui.holder.NewsLiveHolder;
import com.zjrb.daily.news.ui.holder.NewsMultiImageHolder;
import com.zjrb.daily.news.ui.holder.NewsSpecialHolder;
import com.zjrb.daily.news.ui.holder.NewsTextHolder;
import com.zjrb.daily.news.ui.holder.NewsTextImageHolder;
import com.zjrb.daily.news.ui.holder.NewsVideoHolder;

import java.util.List;

/**
 * Created by lixinke on 2017/7/12.
 */

public class ArticleAdapter extends NewsBaseAdapter {

    /**
     * 此处的 type 与 {@link DocType} 没有关系
     */
    private static final int TYPE_TEXT = 1; // 文本
    private static final int TYPE_IMAGE_TEXT = 2; // 图文
    private static final int TYPE_MULTI = 3; // 多图
    private static final int TYPE_LARGE_IMAGE = 4; // 大图

    private static final int TYPE_TOPIC = 5; // 话题
    private static final int TYPE_ACTIVITY = 6; // 活动

    private static final int TYPE_SPECIAL = 7; //  专题

    private static final int TYPE_VIDEO = 8; //  视频
    private static final int TYPE_LIVE = 9; //  直播
    private static final int TYPE_COLUMN = 10; // 推荐栏目

    private static final int TYPE_TEXT_RED = 11; // 文本(红船号)
    private static final int TYPE_IMAGE_TEXT_RED = 12; // 图文（红船号）



    public ArticleAdapter(List datas) {
        super(datas);
    }

    public void updateValue(List<ArticleResponse.DataBean.Article> elements) {
        getData().clear();
        getData().addAll(elements);
        notifyDataSetChanged();
    }

    @Override
    public BaseRecyclerViewHolder onAbsCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_VIDEO: // 视频
                return new NewsVideoHolder(parent);
            case TYPE_LIVE: // 直播
                return new NewsLiveHolder(parent);
            case TYPE_ACTIVITY: // 活动
            case TYPE_TOPIC: // 话题
                return new NewsActivityHolder(parent);
            case TYPE_SPECIAL: // 专题
                return new NewsSpecialHolder(parent);
            case TYPE_COLUMN: // 推荐栏目
                return new NewsColumnHolder(parent);

            case TYPE_IMAGE_TEXT: // 图文
                return new NewsTextImageHolder(parent);
            case TYPE_MULTI: // 多图
                return new NewsMultiImageHolder(parent);
            case TYPE_TEXT: // 纯文本
                return new NewsTextHolder(parent);
            case TYPE_LARGE_IMAGE: // 大图
                return new NewsLargeImageHolder(parent);
            case TYPE_TEXT_RED://红船号文本
                return new RedBoatTextHolder(parent);
            case TYPE_IMAGE_TEXT_RED://红船号图文
                return new RedBoatImageHolder(parent);
            default: // 默认 - 纯文本
                return new NewsTextHolder(parent);
        }
    }

    @Override
    public int getAbsItemViewType(int position) {
        Object data = getData(position);
        int type = 0;
        if (data instanceof ArticleItemBean) {
            ArticleItemBean article = (ArticleItemBean) data;
            switch (article.getDoc_type()) {
                case DocType.VIDEO: // 视频
                    type = TYPE_VIDEO;
                    break;
                case DocType.LIVE: // 直播
                    type = TYPE_LIVE;
                    break;
                case DocType.ACTIVITY: // 活动
                    type = TYPE_ACTIVITY;
                    break;
                case DocType.TOPIC: // 话题
                    type = TYPE_TOPIC;
                    break;
                case DocType.SPECIAL: // 专题
                    type = TYPE_SPECIAL;
                    break;
                case 10://红船号
                {
                    int style = article.getList_style();
                    if (style == ListStyle.IMAGE_TEXT) { // 图文
                        type = TYPE_IMAGE_TEXT_RED;
                    }  else { // 默认纯文本类型
                        type = TYPE_TEXT_RED;
                    }

                    break;
                }
                default:
                    int style = article.getList_style();
                    if (style == ListStyle.IMAGE_TEXT) { // 图文
                        type = TYPE_IMAGE_TEXT;
                    } else if (style == ListStyle.MULTI_IMAGE) { // 多图
                        type = TYPE_MULTI;
                    } else if (style == ListStyle.LARGE_IMAGE){ // 大图
                        type = TYPE_LARGE_IMAGE;
                    } else { // 默认纯文本类型
                        type = TYPE_TEXT;
                    }
                    break;
            }
        } else if (data instanceof ColumnWidget) {
            type = TYPE_COLUMN; // 推荐栏目
        }
        return type;
    }
}
