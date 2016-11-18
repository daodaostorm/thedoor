package com.daodaostorm.thedoor;

import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.daodaostorm.thedoor.common.FirstInfoFactory;
import com.daodaostorm.thedoor.common.HttpEventHandler;
import com.daodaostorm.thedoor.common.ItemInfo;
import com.daodaostorm.thedoor.common.SkinFactory;
import com.daodaostorm.thedoor.common.SkinInfo;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private ListView listView;
    private List<MessageObj> mData;
    private Toolbar toolbar;
    private List<String> choices;
    private List<Integer> choiceIcon;
    private MyAdapter recyclerAdapter;
	
	private FirstInfoFactory mfirstFactory;
	
	private int numCount;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    swipeRefreshLayout.setRefreshing(false);
                    //recyclerAdapter.notifyDataSetChanged();
					numCount = numCount + 1;
					MessageObj obj1 = new MessageObj("" + numCount, "神盾局" + numCount, 
                "神盾局，全称为国土战略防御攻击与后勤保障局，由斯坦·李与杰克·科比联合创造。神盾局是国际安全理事会专门用于处理各种奇异事件的特殊部队", "http://img.shitouer.com/game/recommend/gg.jpg");
					recyclerAdapter.add(obj1, 0);
                    recyclerView.scrollToPosition(0);
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		numCount = 1;
		
		mfirstFactory = new FirstInfoFactory(this);
		mfirstFactory.setHttpEventHandler(firstHandler);
        mfirstFactory.DownloadDatas();
        initData();
        initViews();

    }

	private HttpEventHandler<ArrayList<ItemInfo>> firstHandler = new HttpEventHandler<ArrayList<ItemInfo>>() {

		@Override
		public void HttpSucessHandler(ArrayList<ItemInfo> result) {

			if (result == null)
				return;
			Toast.makeText(MainActivity.this, "Count :" + result.size(), Toast.LENGTH_LONG).show();
			for (int i = 0; i < result.size(); i++){
				//Log.i(TAG, "result : " + result.get(i).title);
				MessageObj obj1 = new MessageObj(result.get(i).id, result.get(i).title, result.get(i).text, result.get(i).img);
				recyclerAdapter.add(obj1, 0);
			}
			recyclerView.scrollToPosition(0);
		}

		@Override
		public void HttpFailHandler() {


		}
	};
	

    private void initViews() {
        /**
         *  初始化Toolbar
         */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("首页");
        //设置导航图标、添加菜单点击事件要在setSupportActionBar方法之后
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_drawer_home);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_search:
                        Toast.makeText(MainActivity.this, "Search !", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.action_notifications:
                        Toast.makeText(MainActivity.this, "Notification !", Toast.LENGTH_LONG).show();
                        break;
                }
                return true;
            }
        });

        /**
         *  初始化RecyclerView
         */
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdapter = new MyAdapter(mData);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.setOnItemClickListener(new MyAdapter.OnRecyclerViewItemClickListener(){
            @Override
            public void onItemClick(View view, String id) {
                Log.i(TAG, "onItemClick : " + id);
				Toast.makeText(MainActivity.this, "onItemClick ! " + id, Toast.LENGTH_LONG).show();
            }
        });
        /**
         *  初始化swipeRefreshLayout
         */
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefreshlayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.color_blue);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Collections.reverse(mData);
                        try {
                            Thread.sleep(1000); //模拟耗时操作
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mHandler.sendEmptyMessage(1);
                    }
                }).start();
            }
        });

        /**
         *  初始化侧滑菜单 DrawerLayout
         */
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close
        );
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        listView = (ListView) findViewById(R.id.listview);
        ListAdapter adapter = new MenuAdapter(this, choices, choiceIcon);
        listView.setAdapter(adapter);
    }

    private void initData() {
        mData = new ArrayList<MessageObj>();
        MessageObj obj1 = new MessageObj("100", "神盾局", "神盾局，全称为国土战略防御攻击与后勤保障局，由斯坦·李与杰克·科比联合创造。神盾局是国际安全理事会专门用于处理各种奇异事件的特殊部队", "http://img.shitouer.com/game/recommend/gg.jpg");
        mData.add(obj1);
       
        choices = new ArrayList<String>();
        choiceIcon = new ArrayList<>();
        choices.add("首页");
        choices.add("发现");
        //choices.add("关注");
        //choices.add("收藏");
        //choices.add("圆桌");
        //choices.add("私信");
        choiceIcon.add(R.mipmap.ic_main);
        choiceIcon.add(R.mipmap.ic_find);
        //choiceIcon.add(R.mipmap.ic_attention);
        //choiceIcon.add(R.mipmap.ic_collect);
        //choiceIcon.add(R.mipmap.ic_circle);
        //choiceIcon.add(R.mipmap.ic_message);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
