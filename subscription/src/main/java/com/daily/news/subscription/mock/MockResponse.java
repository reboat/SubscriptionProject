package com.daily.news.subscription.mock;

import com.daily.news.subscription.model.Recommend;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    public List<Recommend> getRecommedResponse() {
        List<Recommend> recommends = new ArrayList<>();
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
            Recommend recommend = new Recommend();
            recommend.articleCount = random.nextInt();
            for (int j = 0; j < 4; j++) {
                recommend.name += names[random.nextInt(names.length)];
            }

            recommend.subscribeCount = random.nextInt();
            recommend.picUrl = imags[random.nextInt(imags.length)];
            recommends.add(recommend);
        }
        return recommends;
    }
}
