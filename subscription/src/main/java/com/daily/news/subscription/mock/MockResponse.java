package com.daily.news.subscription.mock;

import com.daily.news.subscription.more.column.Column;
import com.daily.news.subscription.home.Focus;
import com.daily.news.subscription.home.SubscriptionResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created by lixinke on 2017/7/11.
 */

public class MockResponse {
    private static final MockResponse ourInstance = new MockResponse();

    public static MockResponse getInstance() {
        return ourInstance;
    }

    private MockResponse() {
    }

    public SubscriptionResponse getSubscriptionResponse() {
        SubscriptionResponse subscription = new SubscriptionResponse();
        subscription.code = 1;
        subscription.request_id = UUID.randomUUID().toString();
        subscription.message = "mock subscription info";
        subscription.data = new SubscriptionResponse.DataBean();
        subscription.data.has_subscribe = false;
        subscription.data.focus_list = getFocusResponse();
        subscription.data.recommend_list = getRecommedResponse();
        return subscription;
    }


    public List<Column> getRecommedResponse() {
        List<Column> recommends = new ArrayList<>();
        String[] names = {"发", "浙", "报", "人", "现", "锄", "额", "和"};
        String[] imags = {
                "http://img3.imgtn.bdimg.com/it/u=826828499,2412343960&fm=26&gp=0.jpg",
                "http://img2.imgtn.bdimg.com/it/u=3732184319,1857452749&fm=26&gp=0.jpg",
                "http://img0.imgtn.bdimg.com/it/u=2767677514,459923336&fm=26&gp=0.jpg",
                "http://img1.imgtn.bdimg.com/it/u=638461789,1813123122&fm=26&gp=0.jpg",
                "http://img3.imgtn.bdimg.com/it/u=1679448770,3520010627&fm=26&gp=0.jpg"
        };
        Random random = new Random();
        for (int i = 0; i < 30; i++) {
            Column recommend = new Column();
            recommend.article_count = random.nextInt();
            for (int j = 0; j < 4; j++) {
                recommend.name += names[random.nextInt(names.length)];
            }

            recommend.subscribe_count = random.nextInt();
            recommend.pic_url = imags[random.nextInt(imags.length)];
            recommend.uid = UUID.randomUUID().toString();
            recommends.add(recommend);
        }
        return recommends;
    }

    public List<Focus> getFocusResponse() {
        String[] titles = {
                "空山新雨后,天气晚来秋",
                "落霞与孤鹜齐飞,秋水共长天一色",
                "天生我材必有用",
                "海上生明月"
        };
        String[] picUrl = {
                "http://img5.imgtn.bdimg.com/it/u=1397660481,1798130402&fm=26&gp=0.jpg",
                "http://img1.imgtn.bdimg.com/it/u=2382350308,4278424087&fm=26&gp=0.jpg",
                "http://img3.imgtn.bdimg.com/it/u=2121918424,1843211123&fm=26&gp=0.jpg",
                "http://img0.imgtn.bdimg.com/it/u=4032300341,2620734131&fm=26&gp=0.jpg"
        };
        List<Focus> focuses = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Focus focus = new Focus();
            focus.doc_title = titles[i];
            focus.pic_url = picUrl[i];
            focuses.add(focus);
        }
        return focuses;
    }
}
