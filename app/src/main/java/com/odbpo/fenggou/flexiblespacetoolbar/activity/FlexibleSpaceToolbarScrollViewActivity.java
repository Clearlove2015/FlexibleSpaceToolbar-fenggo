/*
 * Copyright 2014 Soichiro Kashima
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.odbpo.fenggou.flexiblespacetoolbar.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nineoldandroids.view.ViewHelper;
import com.odbpo.fenggou.flexiblespacetoolbar.R;
import com.odbpo.fenggou.flexiblespacetoolbar.scroll.ObservableScrollView;
import com.odbpo.fenggou.flexiblespacetoolbar.scroll.ObservableScrollViewCallbacks;
import com.odbpo.fenggou.flexiblespacetoolbar.scroll.ScrollState;
import com.odbpo.fenggou.flexiblespacetoolbar.scroll.ScrollUtils;
import com.odbpo.fenggou.flexiblespacetoolbar.util.CommonUtil;

public class FlexibleSpaceToolbarScrollViewActivity extends BaseActivity implements ObservableScrollViewCallbacks,View.OnClickListener {

    private ImageView ivBack;
    private View mFlexibleSpaceView;
    private View mToolbarView;
    private TextView mTitleView;
    private TextView tvDaDa;
    private int mFlexibleSpaceHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flexiblespacetoolbarscrollview);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivBack.setOnClickListener(this);

        mFlexibleSpaceView = findViewById(R.id.flexible_space);
        mTitleView = (TextView) findViewById(R.id.title);
        mTitleView.setOnClickListener(this);
        tvDaDa = (TextView) findViewById(R.id.tv_dada);
        tvDaDa.setOnClickListener(this);
//        mTitleView.setText(getTitle());
        mTitleView.setText("订单状态");
        tvDaDa.setText("达达状态");
        setTitle(null);
        mToolbarView = findViewById(R.id.toolbar);

        final ObservableScrollView scrollView = (ObservableScrollView) findViewById(R.id.scroll);
        scrollView.setScrollViewCallbacks(this);

        mFlexibleSpaceHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_height);
        //System.out.println("actionbar:" + getActionBarSize());//168
        //System.out.println("dp:" + CommonUtil.px2dip(this, 168));//56
        int flexibleSpaceAndToolbarHeight = mFlexibleSpaceHeight + CommonUtil.dip2px(this, 45);

        findViewById(R.id.body).setPadding(0, flexibleSpaceAndToolbarHeight, 0, 0);
        mFlexibleSpaceView.getLayoutParams().height = flexibleSpaceAndToolbarHeight;

        ScrollUtils.addOnGlobalLayoutListener(mTitleView, new Runnable() {
            @Override
            public void run() {
                updateFlexibleSpaceText(scrollView.getCurrentScrollY());
            }
        });
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        updateFlexibleSpaceText(scrollY);
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }

    private void updateFlexibleSpaceText(final int scrollY) {
        //System.out.println("scrollY:" + scrollY);
        //平移
        ViewHelper.setTranslationY(mFlexibleSpaceView, -scrollY);
        int adjustedScrollY = (int) ScrollUtils.getFloat(scrollY, 0, mFlexibleSpaceHeight);
        float maxScale = (float) (mFlexibleSpaceHeight - mToolbarView.getHeight()) / mToolbarView.getHeight();
        float scale = maxScale * ((float) mFlexibleSpaceHeight - adjustedScrollY) / mFlexibleSpaceHeight;

        //pivotX和pivotY：这些属性控制枢轴点的位置，围绕该枢轴点进行旋转和缩放变换。默认情况下，轴心点位于对象中心的中心。
        ViewHelper.setPivotX(mTitleView, 0);
        ViewHelper.setPivotY(mTitleView, 0);
        //scaleX和scaleY：这些属性控制View围绕其轴心点的2D缩放。
        ViewHelper.setScaleX(mTitleView, 1 + scale * 3);
        ViewHelper.setScaleY(mTitleView, 1 + scale * 3);
        int maxTitleTranslationY = mToolbarView.getHeight() + mFlexibleSpaceHeight - (int) (mTitleView.getHeight() * (1 + scale));
        int titleTranslationY = (int) (maxTitleTranslationY * ((float) mFlexibleSpaceHeight - adjustedScrollY) / mFlexibleSpaceHeight);
        ViewHelper.setTranslationY(mTitleView, titleTranslationY * 2 / 3);
        System.out.println("x:" + titleTranslationY);
        ViewHelper.setTranslationX(mTitleView, -titleTranslationY * 3);
        ViewHelper.setTranslationY(tvDaDa, titleTranslationY * 7 / 6);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                Toast.makeText(FlexibleSpaceToolbarScrollViewActivity.this, "back", Toast.LENGTH_SHORT).show();
                break;
            case R.id.title:
                Toast.makeText(FlexibleSpaceToolbarScrollViewActivity.this, "title", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_dada:
                Toast.makeText(FlexibleSpaceToolbarScrollViewActivity.this, "tv_dada", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
