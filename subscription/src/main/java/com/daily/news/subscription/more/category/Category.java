package com.daily.news.subscription.more.category;

import com.daily.news.subscription.more.column.ColumnResponse;

import java.util.List;

public class Category {
    public String class_name;
    public int class_id;
    public boolean is_selected = false;
    public double class_sort_number;
    public List<ColumnResponse.DataBean.ElementsBean> columns;
}

