package com.daily.news.subscription.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
* 用于联合外层coordnorlayout，解决滑动冲突问题
* @author zhengy
* create at 2019/3/4 下午2:11
**/
public class DailyNestedScrollView extends NestedScrollView {
    private String TAG = DailyNestedScrollView.class.getSimpleName();

    /**
     * 该控件滑动的高度，高于这个高度后交给子滑动控件
     */
    int mParentScrollHeight ;
    int mScrollY ;
    /**
     * 嵌套coordnorlayout时，传入一个滑动结束后的toolbar底部到屏幕顶端的距离，用于协调不同位置滑动的时机
     */
    int behavior_Y;
    public DailyNestedScrollView(Context context) {
        super(context);
    }

    public DailyNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DailyNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setMyScrollHeight(int scrollLength) {
        this.mParentScrollHeight = scrollLength;
    }

    public void setBehavior_Y(int Y){
        behavior_Y = Y;
    }




    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        super.onNestedPreScroll(target, dx, dy, consumed, type);
        int[] location = new int[2];
        getLocationOnScreen(location);

        /**
         * location[1] == behavior_Y是为了防止向上滑动时nest和外层一起滑动，设置之后等外层滑动完成后再执行nest的滑动
         * mScrollY > 0 && dy < 0 如果不设置，则下拉到底部后，外层无法收到联动滑动的监听
         */
        boolean isMove = (location[1] == behavior_Y && mScrollY < mParentScrollHeight && dy > 0) || (mScrollY < mParentScrollHeight && mScrollY > 0 && dy < 0);
        if (isMove) {
            consumed[0] = dx;
            consumed[1] = dy;
            scrollBy(0, dy);
        }


    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        mScrollY = t;
    }


}
