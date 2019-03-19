package com.dyhdyh.widget.swiperefresh.example;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.dyhdyh.view.prerecyclerview.LoadMoreFooter;
import com.dyhdyh.view.prerecyclerview.OnLoadMoreListener;
import com.dyhdyh.view.prerecyclerview.PreRecyclerView;
import com.dyhdyh.widget.swiperefresh.example.adapter.ExampleAdapter;
import com.dyhdyh.widget.swiperefresh.example.adapter.ExampleModel;

import java.util.List;

/**
 * author  dengyuhan
 * created 2017/7/6 11:43
 */
public class LoadMoreActivity extends AppCompatActivity {
    private RadioGroup rg_option;
    private RadioButton rb_success;
    private PreRecyclerView rv;
    private ExampleAdapter mExampleAdapter;

    private int mLoadMoreState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadmore);
        rv = findViewById(R.id.rv);
        rg_option = findViewById(R.id.rg_option);
        rg_option.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (R.id.rb_error == checkedId) {
                    mLoadMoreState = LoadMoreFooter.STATE_ERROR;
                } else if (R.id.rb_end == checkedId) {
                    mLoadMoreState = LoadMoreFooter.STATE_THE_END;
                } else {
                    mLoadMoreState = LoadMoreFooter.STATE_NORMAL;
                }
            }
        });
        rb_success = findViewById(R.id.rb_success);
        rb_success.setChecked(true);

        mExampleAdapter = new ExampleAdapter(ExampleData.random(20));
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(mExampleAdapter);
        rv.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.d("---------->", "onLoadMore--->");
                asyncLoadPageData();
            }
        });
    }

    private void asyncLoadPageData() {
        new ExampleAsyncTask() {
            @Override
            protected void onPostExecute(List<ExampleModel> result) {
                rv.setLoadMoreCompleted();
                if (rb_success.isChecked()) {
                    int dataSize = mExampleAdapter.getData().size();
                    mExampleAdapter.getData().addAll(result);
                    mExampleAdapter.notifyItemRangeInserted(dataSize, result.size());
                }
                rv.setLoadMoreState(mLoadMoreState);
            }
        }.execute(10);
    }

/*
    public void clickOnePage(MenuItem menuItem) {
        mExampleAdapter.setData(ExampleData.random(3));
        mExampleAdapter.notifyDataSetChanged();
    }

    public void clickLoadPageData(MenuItem menuItem) {
        mExampleAdapter = new ExampleAdapter(ExampleData.random(10));
        mLoadMoreHelper.setAdapter(mExampleAdapter);
        mLoadMoreHelper.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new ExampleAsyncTask() {
                    @Override
                    protected void onPostExecute(List<ExampleModel> result) {
                        Toast.makeText(LoadMoreActivity.this, "成功加载新的一页", Toast.LENGTH_SHORT).show();
                        int dataSize = mExampleAdapter.getData().size();
                        mExampleAdapter.getData().addAll(result);
                        mExampleAdapter.notifyItemRangeInserted(dataSize, result.size());
                        mLoadMoreHelper.setLoadMore(false);
                    }
                }.execute(10);
            }
        });
    }


    public void clickTheEndData(MenuItem menuItem) {
        mExampleAdapter = new ExampleAdapter(ExampleData.random(10));
        mLoadMoreHelper.setAdapter(mExampleAdapter);
        mLoadMoreHelper.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new ExampleAsyncTask() {
                    @Override
                    protected void onPostExecute(List<ExampleModel> result) {
                        mLoadMoreHelper.setLoadMoreState(LoadMoreFooter.State.THE_END);
                        mLoadMoreHelper.setLoadMore(false);
                    }
                }.execute(10);
            }
        });
    }


    public void clickErrorData(MenuItem menuItem) {
        mExampleAdapter = new ExampleAdapter(ExampleData.random(10));
        rv.setAdapter(mExampleAdapter);
        rv.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new ExampleAsyncTask() {
                    @Override
                    protected void onPostExecute(List<ExampleModel> result) {
                        try {
                            super.onPostExecute(result);
                            mLoadMoreHelper.setLoadMoreState(LoadMoreFooter.State.THE_END);
                        }catch (Exception e){
                            mLoadMoreHelper.setLoadMoreState(LoadMoreFooter.State.ERROR);
                            e.printStackTrace();
                        }finally {
                            mLoadMoreHelper.setLoadMore(false);
                        }
                    }
                }.execute(-1);
            }
        });
    }
*/

    public void clickSetHeaderView(MenuItem menuItem) {
        TextView headerView = new TextView(this);
        headerView.setGravity(Gravity.CENTER);
        headerView.setTextColor(Color.WHITE);
        headerView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150));
        headerView.setBackgroundColor(ExampleData.randomColor());
        headerView.setText("Header");
        rv.setHeaderView(headerView);
    }

    public void clickRemoveHeaderView(MenuItem item) {
        rv.removeHeaderView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.loadmore_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void clickLinearLayoutManager(View view) {
        rv.setLayoutManager(new LinearLayoutManager(this));
    }

    public void clickGridLayoutManager(View view) {
        rv.setLayoutManager(new GridLayoutManager(this, 3));
    }

    public void clickStaggeredGridLayoutManager(View view) {
        mExampleAdapter.setStaggeredGrid(true);
        rv.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
    }

}