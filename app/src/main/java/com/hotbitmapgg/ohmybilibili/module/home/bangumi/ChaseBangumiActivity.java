package com.hotbitmapgg.ohmybilibili.module.home.bangumi;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.hotbitmapgg.ohmybilibili.R;
import com.hotbitmapgg.ohmybilibili.adapter.ChaseBangumiAdapter;
import com.hotbitmapgg.ohmybilibili.base.RxAppCompatBaseActivity;
import com.hotbitmapgg.ohmybilibili.entity.user.UserChaseBangumiInfo;
import com.hotbitmapgg.ohmybilibili.network.RetrofitHelper;
import com.hotbitmapgg.ohmybilibili.widget.CircleProgressView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hcc on 2016/10/14 13:21
 * 100332338@qq.com
 * <p>
 * 追番界面
 */

public class ChaseBangumiActivity extends RxAppCompatBaseActivity
{

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.recycle)
    RecyclerView mRecyclerView;

    @Bind(R.id.circle_progress)
    CircleProgressView mCircleProgressView;

    private static final int MID = 9467159;

    private List<UserChaseBangumiInfo.DataBean.ResultBean> chaseBangumis = new ArrayList<>();

    private ChaseBangumiAdapter mAdapter;

    @Override
    public int getLayoutId()
    {

        return R.layout.activity_chase_bangumi;
    }

    @Override
    public void initViews(Bundle savedInstanceState)
    {

        initRecyclerView();
        getChaseBangumis();
    }

    private void getChaseBangumis()
    {

        RetrofitHelper.getUserChaseBangumiApi()
                .getUserChaseBangumis(MID)
                .compose(bindToLifecycle())
                .doOnSubscribe(this::showProgressBar)
                .map(userChaseBangumiInfo -> userChaseBangumiInfo.getData().getResult())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultBeans -> {

                    chaseBangumis.addAll(resultBeans);
                    finishTask();
                }, throwable -> {

                });
    }

    private void finishTask()
    {

        hideProgressBar();
        mAdapter.notifyDataSetChanged();
    }


    private void initRecyclerView()
    {

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ChaseBangumiActivity.this));
        mAdapter = new ChaseBangumiAdapter(mRecyclerView, chaseBangumis);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initToolBar()
    {

        mToolbar.setTitle("追番");
        setSupportActionBar(mToolbar);
        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar != null)
            mActionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        getMenuInflater().inflate(R.menu.menu_chase_bangumi, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        if (item.getItemId() == android.R.id.home)
            onBackPressed();

        return super.onOptionsItemSelected(item);
    }

    public void showProgressBar()
    {

        mCircleProgressView.setVisibility(View.VISIBLE);
        mCircleProgressView.spin();
    }

    public void hideProgressBar()
    {

        mCircleProgressView.setVisibility(View.GONE);
        mCircleProgressView.stopSpinning();
    }
}
