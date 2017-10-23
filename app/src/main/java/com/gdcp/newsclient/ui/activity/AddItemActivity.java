package com.gdcp.newsclient.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdcp.newsclient.R;
import com.gdcp.newsclient.adapter.AddedColumnAdapter;
import com.gdcp.newsclient.adapter.NotAddColumnAdapter;
import com.gdcp.newsclient.bean.AddItem;
import com.gdcp.newsclient.listener.ClickListener;
import com.gdcp.newsclient.view.MyRecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

;

public class AddItemActivity extends BaseActivity implements ClickListener{


    @BindView(R.id.tv_commit)
    TextView tvCommit;
    @BindView(R.id.toolbal)
    Toolbar toolbal;
    @BindView(R.id.addedRecyclerView)
    MyRecyclerView addedRecyclerView;
    @BindView(R.id.notAddedRecyclerView)
    MyRecyclerView notAddedRecyclerView;
    private List<AddItem> addList;
    private List<AddItem> notAddList;
    private AddedColumnAdapter addedColumnAdapter;
    private NotAddColumnAdapter notAddColumnAdapter;

    @Override
    protected void initData() {
        SharedPreferences sharedPreferences=getSharedPreferences("addItemList", Context.MODE_PRIVATE);
        String json=sharedPreferences.getString("addItemList","");
        Gson gson=new Gson();
        List<AddItem> addItemList=gson.fromJson(json,new TypeToken<List<AddItem>>(){}.getType());
        for (int i = 0; i <addItemList.size() ; i++) {
              if (addItemList.get(i).isAdded()){
                  addList.add(addItemList.get(i));
              }else {
                  notAddList.add(addItemList.get(i));
              }
        }

        addedColumnAdapter.notifyDataSetChanged();
        notAddColumnAdapter.notifyDataSetChanged();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_add_item;
    }

    @Override
    protected void initView() {
        initToolBar();
        addList=new ArrayList<>();
        notAddList=new ArrayList<>();
        addedColumnAdapter=new AddedColumnAdapter(addList,this);
        notAddColumnAdapter=new NotAddColumnAdapter(notAddList,this);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,3);
        GridLayoutManager gridLayoutManager2=new GridLayoutManager(this,3);
        addedRecyclerView.setLayoutManager(gridLayoutManager);
        notAddedRecyclerView.setLayoutManager(gridLayoutManager2);
        addedRecyclerView.setAdapter(addedColumnAdapter);
        notAddedRecyclerView.setAdapter(notAddColumnAdapter);
        addedColumnAdapter.setClickListener(this);
        notAddColumnAdapter.setClickListener(this);
    }

    private void initToolBar() {
        setSupportActionBar(toolbal);
        toolbal.setTitleTextColor(Color.WHITE);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @OnClick({R.id.tv_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_commit:
                update();
                finish();
                break;

        }
    }

    private void update() {
        SharedPreferences sharedPreferences=getSharedPreferences("addItemList", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        Gson gson=new Gson();
        List<AddItem>list=new ArrayList<>();
        for (int i = 0; i < addList.size(); i++) {
            AddItem addItem=new AddItem();
            addItem.setAdded(true);
            addItem.setTitle(addList.get(i).getTitle());
            addItem.setChannelId(addList.get(i).getChannelId());
            list.add(addItem);
        }

        for (int i = 0; i < notAddList.size(); i++) {
            AddItem addItem=new AddItem();
            addItem.setAdded(false);
            addItem.setTitle(notAddList.get(i).getTitle());
            addItem.setChannelId(notAddList.get(i).getChannelId());
            list.add(addItem);
        }
        String json=gson.toJson(list);
        editor.putString("addItemList",json);
        editor.commit();
    }


    @Override
    public void del(AddItem s, int i) {
         //从已经添加中的项目中移除，添加到未添加中的项目
        addList.remove(s);
        notAddList.add(s);
        addedColumnAdapter.notifyDataSetChanged();
        notAddColumnAdapter.notifyDataSetChanged();
    }

    @Override
    public void add(AddItem s, int position) {
        //从已经未添加中的项目中移除，添加到已添加中的项目
        notAddList.remove(s);
        addList.add(s);
        addedColumnAdapter.notifyDataSetChanged();
        notAddColumnAdapter.notifyDataSetChanged();
    }



/*public void change(){
      final String[] titles = new String[] {
                "头条", "社会", "科技", "财经", "体育", "汽车","历史","军事","娱乐","游戏","智能","手机"
        };
        final String[] channelId = new String[] {
                "T1348647909107",
                "T1348648037603",
                "T1348649580692",
                "T1348648756099",
                "T1348649079062",
                "T1348654060988",
                "T1368497029546",
                "T1348648141035",
                "T1348648517839",
                "T1348654151579",
                "T1351233117091",
                "T1348649654285"
        };
    SharedPreferences sharedPreferences=getSharedPreferences("addItemList", Context.MODE_PRIVATE);
    SharedPreferences.Editor editor=sharedPreferences.edit();
    Gson gson=new Gson();
    List<AddItem>list=new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            AddItem addItem=new AddItem();
            addItem.setAdded(true);
            addItem.setChannelId(channelId[i]);
            addItem.setTitle(titles[i]);
            list.add(addItem);
        }


    String json=gson.toJson(list);
    editor.putString("addItemList",json);
    editor.commit();


}*/


}