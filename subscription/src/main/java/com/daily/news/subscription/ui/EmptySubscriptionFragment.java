package com.daily.news.subscription.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EmptySubscriptionFragment extends Fragment {

    @BindView(R2.id.empty_subscription_container)
    FrameLayout mContainer;
    @BindView(R2.id.empty_subscription_progress_container)
    View mProgressContainer;

    public EmptySubscriptionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_empty_subscription, container, false);
        ButterKnife.bind(root);
        return root;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
