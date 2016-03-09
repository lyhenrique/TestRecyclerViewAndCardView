package com.ericlau.testrecyclerviewandcardview;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.cundong.recyclerview.EndlessRecyclerOnScrollListener;
import com.cundong.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.cundong.recyclerview.HeaderSpanSizeLookup;
import com.ericlau.testrecyclerviewandcardview.adapter.ItemBean;
import com.ericlau.testrecyclerviewandcardview.adapter.MyAdapter;
import com.ericlau.testrecyclerviewandcardview.utils.NetworkUtils;
import com.ericlau.testrecyclerviewandcardview.utils.RecyclerViewStateUtils;
import com.ericlau.testrecyclerviewandcardview.widget.LoadingFooter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by cundong on 2015/10/29.
 *
 * 带HeaderView的分页加载GridLayout RecyclerView
 */
public class MainActivity extends AppCompatActivity {

    /**
     * 服务器端一共多少条数据
     */
    private static final int TOTAL_COUNTER = 64;

    /**
     * 每一页展示多少条数据
     */
    private static final int REQUEST_COUNT = 10;

    /**
     * 已经获取到多少条数据了
     */
    private int mCurrentCounter = 0;

    private RecyclerView mRecyclerView = null;

    private MyAdapter mDataAdapter = null;

    private List<ItemBean> itemBeanList = new ArrayList<ItemBean>();

    private PreviewHandler mHandler = new PreviewHandler(this);
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Sales");
        //显示系统Actionbar的返回按钮
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.list);

        //init data
       initData();

        mCurrentCounter = itemBeanList.size();

        mDataAdapter = new MyAdapter(this,itemBeanList);

        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);

        //setLayoutManager
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        manager.setSpanSizeLookup(new HeaderSpanSizeLookup((HeaderAndFooterRecyclerViewAdapter) mRecyclerView.getAdapter(), manager.getSpanCount()));
        mRecyclerView.setLayoutManager(manager);

//        RecyclerViewUtils.setHeaderView(mRecyclerView, new SampleHeader(this));

        mRecyclerView.addOnScrollListener(mOnScrollListener);
        setItemClick();
    }


    /*
    * 之后改到网上读
    * 估计得改好多
    *
    * */
    private void initData() {
        itemBeanList = new ArrayList<>();
        for(int i=0;i<10;i++) {
            itemBeanList.add(new ItemBean(R.mipmap.ic_launcher,
                    "Title" +i,"Content" +i));
        }
    }

    //设置点击事件
    private void setItemClick() {
        mDataAdapter.setOnItemClickListener(new MyAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, ItemBean data) {
                Intent intent = new Intent(MainActivity.this,SalesDetails.class);
                Bundle bundle = new Bundle();
                bundle.putString("Title",data.title);
                bundle.putString("Content",data.content);
                bundle.putInt("Photo",data.photo);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void notifyDataSetChanged() {
        mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void addItems(ArrayList<ItemBean> list) {
        mDataAdapter.addAll(list);
        mCurrentCounter += list.size();
    }

    private EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {

        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);

            LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(mRecyclerView);
            if (state == LoadingFooter.State.Loading) {
                Log.d("BeautyCare", "the state is Loading, just wait..");
                return;
            }

            if (mCurrentCounter < TOTAL_COUNTER) {
                // loading more
                RecyclerViewStateUtils.setFooterViewState(MainActivity.this, mRecyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
                requestData();
            } else {
                //the end
                RecyclerViewStateUtils.setFooterViewState(MainActivity.this, mRecyclerView, REQUEST_COUNT, LoadingFooter.State.TheEnd, null);
            }
        }
    };

    private class PreviewHandler extends Handler {

        private WeakReference<MainActivity> ref;

        PreviewHandler(MainActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final MainActivity activity = ref.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }

            switch (msg.what) {
                case -1:
                    int currentSize = activity.mDataAdapter.getItemCount();
                    //模拟组装10个数据
                    ArrayList<ItemBean> newList = new ArrayList<>();
                    for (int i = 0; i < 10; i++) {
                        if (newList.size() + currentSize >= TOTAL_COUNTER) {
                            break;
                        }

                        newList.add(new ItemBean(R.mipmap.ic_launcher, "Titleadfadf", "Contentadfdafs"));
                    }
                    Log.e("EricLau","newListAdd successfully");
                    activity.addItems(newList);
                    RecyclerViewStateUtils.setFooterViewState(activity.mRecyclerView, LoadingFooter.State.Normal);
                    break;
                case -2:
                    activity.notifyDataSetChanged();
                    Log.e("EricLau", "Case2 notifyDatasetchanged");
                    break;
                case -3:
                    RecyclerViewStateUtils.setFooterViewState(activity, activity.mRecyclerView, REQUEST_COUNT, LoadingFooter.State.NetWorkError, activity.mFooterClick);
                    Log.e("EricLau", "case3 network error");
                    break;
            }
        }
    }

    private View.OnClickListener mFooterClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerViewStateUtils.setFooterViewState(MainActivity.this, mRecyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
            requestData();
        }
    };

    /**
     * 模拟请求网络
     */
    private void requestData() {

        new Thread() {

            @Override
            public void run() {
                super.run();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //模拟一下网络请求失败的情况
                if (NetworkUtils.isNetAvailable(MainActivity.this)) {
                    mHandler.sendEmptyMessage(-1);
                } else {
                    mHandler.sendEmptyMessage(-3);
                }
            }
        }.start();
    }

    //菜单返回上一页
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                finish();
                return true;
        }
        return false;
    }

}