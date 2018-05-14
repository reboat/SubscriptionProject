package com.daily.news.subscription.task;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.zjrb.core.api.base.APIGetTask;
import com.zjrb.core.api.callback.APICallBack;
import com.zjrb.core.api.callback.LoadingCallBack;
import com.zjrb.core.common.biz.ResourceBiz;
import com.zjrb.core.common.manager.APICallManager;
import com.zjrb.core.db.SPHelper;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.utils.StringUtils;


/**
 * Created by lixinke on 2017/10/27.
 */

public class GetInitializeResourceTask extends APIGetTask<ResourceBiz> {

    public static void createTask(Fragment fragment, Object tag) {
        new GetInitializeResourceTask(new ResourceCallBack(fragment)).setTag(tag).exe();
    }

    public static void cancelTask(Object tag) {
        APICallManager.get().cancel(tag);
    }


    public GetInitializeResourceTask(LoadingCallBack<ResourceBiz> callback) {
        super(callback);
    }

    @Override
    protected void onSetupParams(Object... params) {

    }

    @Override
    protected String getApi() {
        return "/api/resource";
    }

    static class ResourceCallBack extends APICallBack<ResourceBiz> {

        Fragment mFragment;

        public ResourceCallBack(Fragment fragment) {
            this.mFragment = fragment;
        }

        @Override
        public void onSuccess(ResourceBiz resourceBiz) {
            try {
                if (resourceBiz != null && resourceBiz.feature_list != null) {
                    int i = 0;
                    for(ResourceBiz.FeatureListBean bean : resourceBiz.feature_list)
                    {
                        if(bean.name.equals("hch"))
                        {
                            i = 1;
                            if(bean.enabled && bean.desc != null && bean.desc != "")
                            {
                                Nav.with(mFragment).to("http://www.8531.cn/subscription/more_new");
                            }
                            else
                            {
                                Nav.with(mFragment).to("http://www.8531.cn/subscription/more");
                            }
                            break;
                        }
                    }
                    if(i == 0)
                    {
                        Nav.with(mFragment).to("http://www.8531.cn/subscription/more");
                    }
                }
                else
                {
                    Nav.with(mFragment).to("http://www.8531.cn/subscription/more");
                }

                SPHelper.get().put(SPHelper.Key.INITIALIZATION_RESOURCES, resourceBiz).commit();

            } catch (Exception e) {
            }
        }

        @Override
        public void onError(String errMsg, int errCode) {
            Nav.with(mFragment).to("http://www.8531.cn/subscription/more");
        }

        @Override
        public void onCancel() {
            Nav.with(mFragment).to("http://www.8531.cn/subscription/more");
        }
    }
}
