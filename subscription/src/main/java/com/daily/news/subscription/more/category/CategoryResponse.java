package com.daily.news.subscription.more.category;

import java.util.List;

/**
 * 订阅栏目数据结构
 */

public class CategoryResponse {
    public int code;
    public String message;
    public String request_id;
    public List<Category> elements;
}

