package com.daily.news.subscription.model;

import java.util.List;

public class Subscription {
    private int code;
    private String message;
    private String request_id;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        private boolean has_subscribe;
        private List<FocusListBean> focus_list;
        private List<RecommendListBean> recommend_list;
        private List<ArticleListBean> article_list;

        public boolean isHas_subscribe() {
            return has_subscribe;
        }

        public void setHas_subscribe(boolean has_subscribe) {
            this.has_subscribe = has_subscribe;
        }

        public List<FocusListBean> getFocus_list() {
            return focus_list;
        }

        public void setFocus_list(List<FocusListBean> focus_list) {
            this.focus_list = focus_list;
        }

        public List<RecommendListBean> getRecommend_list() {
            return recommend_list;
        }

        public void setRecommend_list(List<RecommendListBean> recommend_list) {
            this.recommend_list = recommend_list;
        }

        public List<ArticleListBean> getArticle_list() {
            return article_list;
        }

        public void setArticle_list(List<ArticleListBean> article_list) {
            this.article_list = article_list;
        }

        public static class FocusListBean {

            private String doc_title;
            private String doc_url;
            private String pic_url;
            private int sort_number;

            public String getDoc_title() {
                return doc_title;
            }

            public void setDoc_title(String doc_title) {
                this.doc_title = doc_title;
            }

            public String getDoc_url() {
                return doc_url;
            }

            public void setDoc_url(String doc_url) {
                this.doc_url = doc_url;
            }

            public String getPic_url() {
                return pic_url;
            }

            public void setPic_url(String pic_url) {
                this.pic_url = pic_url;
            }

            public int getSort_number() {
                return sort_number;
            }

            public void setSort_number(int sort_number) {
                this.sort_number = sort_number;
            }
        }

        public static class RecommendListBean {

            private String uid;
            private String name;
            private String pic_url;
            private int subscribe_count;
            private int article_count;

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPic_url() {
                return pic_url;
            }

            public void setPic_url(String pic_url) {
                this.pic_url = pic_url;
            }

            public int getSubscribe_count() {
                return subscribe_count;
            }

            public void setSubscribe_count(int subscribe_count) {
                this.subscribe_count = subscribe_count;
            }

            public int getArticle_count() {
                return article_count;
            }

            public void setArticle_count(int article_count) {
                this.article_count = article_count;
            }
        }

        public static class ArticleListBean {

            private int id;
            private String list_title;
            private Object list_style;
            private String list_tag;
            private int doc_type;
            private int read_count;
            private int like_count;
            private int comment_count;
            private String channel_id;
            private String channel_name;
            private String column_id;
            private String column_name;
            private long sort_number;
            private String uri_scheme;
            private boolean is_followed;
            private boolean is_column_subscribed;
            private int comment_level;
            private boolean is_like_enabled;
            private String web_link;
            private int album_count;
            private String activity_status;
            private long activity_date_begin;
            private long activity_date_end;
            private int activity_register_count;
            private boolean activity_announced;
            private String live_type;
            private String live_status;
            private String live_url;
            private String video_url;
            private int video_duration;
            private long topic_date_begin;
            private long topic_date_end;
            private List<String> list_pics;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getList_title() {
                return list_title;
            }

            public void setList_title(String list_title) {
                this.list_title = list_title;
            }

            public Object getList_style() {
                return list_style;
            }

            public void setList_style(Object list_style) {
                this.list_style = list_style;
            }

            public String getList_tag() {
                return list_tag;
            }

            public void setList_tag(String list_tag) {
                this.list_tag = list_tag;
            }

            public int getDoc_type() {
                return doc_type;
            }

            public void setDoc_type(int doc_type) {
                this.doc_type = doc_type;
            }

            public int getRead_count() {
                return read_count;
            }

            public void setRead_count(int read_count) {
                this.read_count = read_count;
            }

            public int getLike_count() {
                return like_count;
            }

            public void setLike_count(int like_count) {
                this.like_count = like_count;
            }

            public int getComment_count() {
                return comment_count;
            }

            public void setComment_count(int comment_count) {
                this.comment_count = comment_count;
            }

            public String getChannel_id() {
                return channel_id;
            }

            public void setChannel_id(String channel_id) {
                this.channel_id = channel_id;
            }

            public String getChannel_name() {
                return channel_name;
            }

            public void setChannel_name(String channel_name) {
                this.channel_name = channel_name;
            }

            public String getColumn_id() {
                return column_id;
            }

            public void setColumn_id(String column_id) {
                this.column_id = column_id;
            }

            public String getColumn_name() {
                return column_name;
            }

            public void setColumn_name(String column_name) {
                this.column_name = column_name;
            }

            public long getSort_number() {
                return sort_number;
            }

            public void setSort_number(long sort_number) {
                this.sort_number = sort_number;
            }

            public String getUri_scheme() {
                return uri_scheme;
            }

            public void setUri_scheme(String uri_scheme) {
                this.uri_scheme = uri_scheme;
            }

            public boolean isIs_followed() {
                return is_followed;
            }

            public void setIs_followed(boolean is_followed) {
                this.is_followed = is_followed;
            }

            public boolean isIs_column_subscribed() {
                return is_column_subscribed;
            }

            public void setIs_column_subscribed(boolean is_column_subscribed) {
                this.is_column_subscribed = is_column_subscribed;
            }

            public int getComment_level() {
                return comment_level;
            }

            public void setComment_level(int comment_level) {
                this.comment_level = comment_level;
            }

            public boolean isIs_like_enabled() {
                return is_like_enabled;
            }

            public void setIs_like_enabled(boolean is_like_enabled) {
                this.is_like_enabled = is_like_enabled;
            }

            public String getWeb_link() {
                return web_link;
            }

            public void setWeb_link(String web_link) {
                this.web_link = web_link;
            }

            public int getAlbum_count() {
                return album_count;
            }

            public void setAlbum_count(int album_count) {
                this.album_count = album_count;
            }

            public String getActivity_status() {
                return activity_status;
            }

            public void setActivity_status(String activity_status) {
                this.activity_status = activity_status;
            }

            public long getActivity_date_begin() {
                return activity_date_begin;
            }

            public void setActivity_date_begin(long activity_date_begin) {
                this.activity_date_begin = activity_date_begin;
            }

            public long getActivity_date_end() {
                return activity_date_end;
            }

            public void setActivity_date_end(long activity_date_end) {
                this.activity_date_end = activity_date_end;
            }

            public int getActivity_register_count() {
                return activity_register_count;
            }

            public void setActivity_register_count(int activity_register_count) {
                this.activity_register_count = activity_register_count;
            }

            public boolean isActivity_announced() {
                return activity_announced;
            }

            public void setActivity_announced(boolean activity_announced) {
                this.activity_announced = activity_announced;
            }

            public String getLive_type() {
                return live_type;
            }

            public void setLive_type(String live_type) {
                this.live_type = live_type;
            }

            public String getLive_status() {
                return live_status;
            }

            public void setLive_status(String live_status) {
                this.live_status = live_status;
            }

            public String getLive_url() {
                return live_url;
            }

            public void setLive_url(String live_url) {
                this.live_url = live_url;
            }

            public String getVideo_url() {
                return video_url;
            }

            public void setVideo_url(String video_url) {
                this.video_url = video_url;
            }

            public int getVideo_duration() {
                return video_duration;
            }

            public void setVideo_duration(int video_duration) {
                this.video_duration = video_duration;
            }

            public long getTopic_date_begin() {
                return topic_date_begin;
            }

            public void setTopic_date_begin(long topic_date_begin) {
                this.topic_date_begin = topic_date_begin;
            }

            public long getTopic_date_end() {
                return topic_date_end;
            }

            public void setTopic_date_end(long topic_date_end) {
                this.topic_date_end = topic_date_end;
            }

            public List<String> getList_pics() {
                return list_pics;
            }

            public void setList_pics(List<String> list_pics) {
                this.list_pics = list_pics;
            }
        }
    }
}
