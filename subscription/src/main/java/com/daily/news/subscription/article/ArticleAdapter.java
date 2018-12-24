package com.daily.news.subscription.article;

import com.zjrb.daily.news.bean.ArticleItemBean;
import com.zjrb.daily.news.ui.adapter.NewsBaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lixinke on 2017/7/12.
 */

public class ArticleAdapter extends NewsBaseAdapter {

    List<ArticleResponse.DataBean.Article> mAdList;

    public ArticleAdapter(List datas) {
        super(datas);
    }

    public void updateValue(List<ArticleResponse.DataBean.Article> elements, List<ArticleResponse.DataBean.Article> beans) {
        mAdList = beans;
        getData().clear();
        getData().addAll(elements);
        handleAd();
        notifyDataSetChanged();
    }

    public void addData(List<ArticleResponse.DataBean.Article> data){
        addData(data, true);
        handleAd();
        notifyDataSetChanged();
    }


    private void  handleAd() {
        if (mAdList==null){
            return;
        }
        ArrayList<ArticleItemBean> addList = new ArrayList<>();//新建要插入的广告集合
        for (int i = 0; i < mAdList.size(); i++) {//遍历所有广告集合 把符合范围内的广告放入新集合
            ArticleItemBean adBean = mAdList.get(i);
            int size = datas.size();
            int position = adBean.adPosition;
            if (position<size){
                addList.add(adBean);
            }
        }
        for (int i = 0; i < addList.size(); i++) {//把新集合中的元素按位置插入列表
            ArticleItemBean adBean = addList.get(i);
            int size = datas.size();
            int position = adBean.adPosition;
            if (position<=size){
                datas.add(position,adBean);
            }
        }
        mAdList.removeAll(addList);//删除已经插入的广告
    }
}
