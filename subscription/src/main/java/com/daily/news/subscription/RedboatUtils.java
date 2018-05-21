package com.daily.news.subscription;

import com.zjrb.core.common.biz.ResourceBiz;
import com.zjrb.core.db.SPHelper;

public class RedboatUtils {


    //动态获取tab的名字，最长4个字符
    public static String getRedboatTitle() {
        ResourceBiz biz = SPHelper.get().getObject(SPHelper.Key.INITIALIZATION_RESOURCES);
        if (biz != null && biz.feature_list != null) {
            for (ResourceBiz.FeatureListBean bean : biz.feature_list) {
                if (bean.name.equals("hch")) {
                    String text = bean.desc;
                    if (text != null && text != "") {

                        if (text.length() > 4) {
                            text = text.substring(0, 4) + "...";

                        }
                        return text;
                    }
                    break;
                }
            }
        }
        return "";
    }
}
