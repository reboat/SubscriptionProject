package com.daily.news.subscription.rank;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daily.news.subscription.R;
import com.daily.news.subscription.R2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.daily.news.biz.core.ui.dialog.BaseBottomDialogFragment;

public class ClassifyDialogFragment extends BaseBottomDialogFragment {

    public static final String CLASSIFYNAME = "classifyName";

    @BindView(R2.id.all_checkbox)
    CheckBox allCheckbox;
    @BindView(R2.id.all_classify)
    RelativeLayout allClassify;
    @BindView(R2.id.zw_checkbox)
    CheckBox zwCheckbox;
    @BindView(R2.id.zw_classify)
    RelativeLayout zwClassify;
    @BindView(R2.id.dt_checkbox)
    CheckBox dtCheckbox;
    @BindView(R2.id.dt_classify)
    RelativeLayout dtClassify;
    @BindView(R2.id.sh_checkbox)
    CheckBox shCheckbox;
    @BindView(R2.id.sh_classify)
    RelativeLayout shClassify;
    @BindView(R2.id.tv_cancel)
    TextView tvCancel;
    private AlertDialog mDialog;

    private OnClassifySelectListener listener;
    String classifName;

    public static ClassifyDialogFragment instance(String classifyName) {
        ClassifyDialogFragment fragment = new ClassifyDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CLASSIFYNAME, classifyName);
        fragment.setArguments(bundle);
        return fragment;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.BottomDialog);
        View contentView = View.inflate(getContext(), R.layout.subscription_bottom_classify, null);
        ButterKnife.bind(this, contentView);
        builder.setView(contentView);
        mDialog = builder.create();
        mDialog.setCanceledOnTouchOutside(true);
        classifName = getArguments().getString(CLASSIFYNAME);
        if ("总榜".equals(classifName)) {
            allCheckbox.setChecked(true);
        } else if ("政务榜".equals(classifName)) {
            zwCheckbox.setChecked(true);
        } else if ("党团榜".equals(classifName)) {
            dtCheckbox.setChecked(true);
        } else if ("生活榜".equals(classifName)) {
            shCheckbox.setChecked(true);
        }
        return mDialog;
    }


    public void setClassifySelectListener(OnClassifySelectListener listener) {
        this.listener = listener;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R2.id.all_classify, R2.id.zw_classify, R2.id.dt_classify, R2.id.sh_classify, R2.id.tv_cancel})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.tv_cancel) {
            mDialog.dismiss();
            return;

        }
        if (listener == null) {
            return;
        }
        if (id == R.id.all_classify) {
            classifName = "总榜";
            listener.select(id, classifName);
            allCheckbox.setChecked(true);
            zwCheckbox.setChecked(false);
            dtCheckbox.setChecked(false);
            shCheckbox.setChecked(false);
            mDialog.dismiss();

        } else if (id == R.id.zw_classify) {
            classifName = "政务榜";
            listener.select(id, classifName);
            allCheckbox.setChecked(false);
            zwCheckbox.setChecked(true);
            dtCheckbox.setChecked(false);
            shCheckbox.setChecked(false);
            mDialog.dismiss();
        } else if (id == R.id.dt_classify) {
            classifName = "党团榜";
            listener.select(id, classifName);
            allCheckbox.setChecked(false);
            zwCheckbox.setChecked(false);
            dtCheckbox.setChecked(true);
            shCheckbox.setChecked(false);
            mDialog.dismiss();
        } else if (id == R.id.sh_classify) {
            classifName = "生活榜";
            listener.select(id, classifName);
            allCheckbox.setChecked(false);
            zwCheckbox.setChecked(false);
            dtCheckbox.setChecked(false);
            shCheckbox.setChecked(true);
            mDialog.dismiss();
        }

    }


    public interface OnClassifySelectListener {
        void select(@IdRes int id, String classifName);
    }
}
