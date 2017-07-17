package com.daily.news.subscription.more;

import java.util.List;

/**
 * Created by lixinke on 2017/7/17.
 */

public class CategoryBean {
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
        private List<ElementsBean> elements;

        public List<ElementsBean> getElements() {
            return elements;
        }

        public void setElements(List<ElementsBean> elements) {
            this.elements = elements;
        }

        public static class ElementsBean {
            private String class_name;
            private int class_id;
            private int class_sort_number;
            private List<ColumnsBean> columns;

            public String getClass_name() {
                return class_name;
            }

            public void setClass_name(String class_name) {
                this.class_name = class_name;
            }

            public int getClass_id() {
                return class_id;
            }

            public void setClass_id(int class_id) {
                this.class_id = class_id;
            }

            public int getClass_sort_number() {
                return class_sort_number;
            }

            public void setClass_sort_number(int class_sort_number) {
                this.class_sort_number = class_sort_number;
            }

            public List<ColumnsBean> getColumns() {
                return columns;
            }

            public void setColumns(List<ColumnsBean> columns) {
                this.columns = columns;
            }

            public static class ColumnsBean {
                private String uid;
                private String name;
                private String pic_url;
                private int subscribe_count;
                private int article_count;
                private boolean subscribed;
                private int sort_number;

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

                public boolean isSubscribed() {
                    return subscribed;
                }

                public void setSubscribed(boolean subscribed) {
                    this.subscribed = subscribed;
                }

                public int getSort_number() {
                    return sort_number;
                }

                public void setSort_number(int sort_number) {
                    this.sort_number = sort_number;
                }
            }
        }
    }
}
