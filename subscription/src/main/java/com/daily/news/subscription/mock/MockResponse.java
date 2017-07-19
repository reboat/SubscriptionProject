package com.daily.news.subscription.mock;

import com.daily.news.subscription.Article;
import com.daily.news.subscription.detail.DetailColumn;
import com.daily.news.subscription.detail.DetailFragment;
import com.daily.news.subscription.home.Focus;
import com.daily.news.subscription.home.SubscriptionResponse;
import com.daily.news.subscription.more.column.Column;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static android.R.attr.detailColumn;

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
        subscription.data.has_subscribe = true;
        subscription.data.focus_list = getFocusResponse();
        subscription.data.recommend_list = getRecommedResponse();

        if (subscription.data.has_subscribe) {
            subscription.data.article_list = getArticles();
        }

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

    public List<Article> getArticles() {

        Random random = new Random();
        String[] pics = new String[]{"http://p1.pstatp.com/list/190x124/2ecf0002377810ca0769", "http://p3.pstatp.com/list/190x124/2edd0001120271cbf71d", "http://p1.pstatp.com/list/190x124/2ecf000031175e9ca02b", "http://p1.pstatp.com/list/190x124/2ecf0001d84b708fbc4d", "https://p3.pstatp.com/list/190x124/2edc000118d8ca9fefe4"};

        List<Article> articles = new ArrayList<>();
        for (int i = 0; i < 600; i++) {
            Article article = new Article();
            if (random.nextBoolean()) {
                article.list_title = "习近平主持召开中央财经领导小组第十六次会议";
            } else {
                article.list_title = "习近平主持召开中央财经领导小组第十六次会议习近平主持召开中央财经领导小组第十六次会议习近平主持召开中央财经领导小组第十六次会议习近平主持召开中央财经领导小组第十六次会议习近平主持召开中央财经领导小组第十六次会议";
            }

            article.list_pics = new ArrayList<>();

            if (random.nextBoolean()) {
                article.list_pics.add(pics[random.nextInt(pics.length - 1)]);
            } else {
                for (int k = 0; k < 3; k++) {
                    article.list_pics.add(pics[random.nextInt(pics.length - 1)]);
                }
            }
            article.channel_name = "新闻热点";
            article.read_count = Math.abs(random.nextInt());
            article.like_count = Math.abs(random.nextInt());
            if (random.nextBoolean()) {
                article.video_url = "http://www.365yg.com/group/6437717271228973570/";
            }
            articles.add(article);
        }
        return articles;
    }

    public DetailColumn getDetail(String id) {
        Random random = new Random();
        DetailColumn detailColumn = new DetailColumn();
        detailColumn.pic_url="http://img3.redocn.com/tupian/20150314/shuyediqiutushangyeicon_4007116.jpg";
        detailColumn.background_url="http://imgsrc.baidu.com/image/c0%3Dshijue1%2C0%2C0%2C294%2C40/sign=cc3fc816a5c3793169658e6a83addd30/0b55b319ebc4b745f766bd06c5fc1e178a82152b.jpg";
        detailColumn.article_count = Math.abs(random.nextInt(1000));
        detailColumn.subscribe_count = Math.abs(random.nextInt(1000));
        detailColumn.name = "诗经里的野菜";
        detailColumn.description = "先民根据地球在黄道轨迹上的位置变化制定了二十四节气，每个节气分别相应于地球在黄道上每运动15°所到达的位置。太阳到达黄经315°位置时，正是二十四节气中之“立春";
        detailColumn.background_url = "http://easyread.ph.126.net/Me57p9l34QHfkkYHXDEnzQ==/8796093022385378775.jpg";
        detailColumn.elements = getArticles();
        return detailColumn;
    }
}
