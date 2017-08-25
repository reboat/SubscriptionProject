package com.daily.news.subscription.article;

import java.util.List;

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
        public static class Article {
            public int id;
            public String list_title;
            public Object list_style;
            public String list_tag;
            public int doc_type;
            public int read_count;
            public int like_count;
            public int comment_count;
            public String channel_id;
            public String channel_name;
            public String column_id;
            public String column_name;
            public long sort_number;
            public String uri_scheme;
            public boolean is_followed;
            public boolean is_column_subscribed;
            public int comment_level;
            public boolean is_like_enabled;
            public String web_link;
            public int album_count;
            public String activity_status;
            public long activity_date_begin;
            public long activity_date_end;
            public int activity_register_count;
            public boolean activity_announced;
            public String live_type;
            public String live_status;
            public String live_url;
            public String video_url;
            public int video_duration;
            public long topic_date_begin;
            public long topic_date_end;
            public List<String> list_pics;
        }
    }
}
