package com.daily.news.subscription;

/**
 * 点击事件接口
 */

public interface OnItemClickListener<T> {
    /**
     * 点击事件
     *
     * @param position 点击item的位置
     * @param item     点击item的model
     */
    void onItemClick(int position, T item);
}
