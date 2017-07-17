package com.daily.news.subscription.more;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daily.news.subscription.R;

public class MoreDetailFragment extends Fragment {
    public static final String ARG_ITEM_ID = "item_id";
    private CategoryContent.CategoryItem mItem;

    public MoreDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mItem = CategoryContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.more_detail, container, false);

        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.more_detail)).setText(mItem.details);
        }

        return rootView;
    }
}
