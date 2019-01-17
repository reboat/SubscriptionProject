package com.daily.news.subscription.article;

import com.zjrb.daily.ad.model.AdModel;
import com.zjrb.daily.ad.model.CreateType;
import com.zjrb.daily.news.bean.ArticleItemBean;

import java.util.ArrayList;
import java.util.List;

import static com.zjrb.daily.news.ui.adapter.NewsBaseAdapter.TYPE_IMAGE_TEXT;
import static com.zjrb.daily.news.ui.adapter.NewsBaseAdapter.TYPE_LARGE_IMAGE;
import static com.zjrb.daily.news.ui.adapter.NewsBaseAdapter.TYPE_MULTI;
import static com.zjrb.daily.news.ui.adapter.NewsBaseAdapter.TYPE_TEXT;

/**
 * Created by lixinke on 2017/8/25.
 */

public class ArticleResponse {
    public int code;
    public String message;
    public String request_id;
    public DataBean data;


    public static class DataBean {
        public List<Article> elements;
        public List<Article> adBeans;

        public static class Article extends ArticleItemBean {

            public static ArticleItemBean switchToArticle(AdModel adModel) {
                Article  articleItemBean = new Article();
                articleItemBean.isAd = true;
                if (CreateType.IMGANDTITLE.getName().equals(adModel.getType())){//左图右文
                    articleItemBean.setList_style(TYPE_IMAGE_TEXT);
                }else if (CreateType.BIGIMGANDTITLE.getName().equals(adModel.getType())){//大图+文字
                    articleItemBean.setList_style(TYPE_LARGE_IMAGE);
                }else if (CreateType.THREEIMGTITLE.getName().equals(adModel.getType())){//三图+文字
                    articleItemBean.setList_style(TYPE_MULTI);
                }else if (CreateType.TITLE.getName().equals(adModel.getType())){//纯文字
                    articleItemBean.setList_style(TYPE_TEXT);
                }
                articleItemBean.setUrl(adModel.getClick_url());
                List<String> pics = new ArrayList<>();
                pics.add(adModel.getImageUrlOne());
                pics.add(adModel.getImageUrlTwo());
                pics.add(adModel.getImageUrlThreen());
                articleItemBean.setList_pics(pics);
                articleItemBean.setList_title(adModel.getTitle());
                articleItemBean.adPosition = adModel.getPosition();
                articleItemBean.adTag = adModel.getLabel();
                return articleItemBean;
            }

        }
    }
}
