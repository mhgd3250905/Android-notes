package com.example.recyclerviewdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvCard;
    private CardAdapter adapter;
    private List<CardBean> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataList = new ArrayList<>();
        dataList.add(new CardBean(R.drawable.p1, "title-1", "desc-1", R.color.colorPrimary));
        dataList.add(new CardBean(R.drawable.p2, "title-2", "desc-2", R.color.colorAccent));
        dataList.add(new CardBean(R.drawable.p3, "title-3", "desc-3", R.color.colorPrimary));
        dataList.add(new CardBean(R.drawable.p4, "title-4", "desc-4", R.color.colorAccent));
        dataList.add(new CardBean(R.drawable.p5, "title-5", "desc-5", R.color.colorPrimary));
        dataList.add(new CardBean(R.drawable.p6, "title-6", "desc-6", R.color.colorAccent));
        dataList.add(new CardBean(R.drawable.p7, "title-7", "desc-7", R.color.colorPrimary));
        dataList.add(new CardBean(R.drawable.p8, "title-8", "desc-8", R.color.colorAccent));
        dataList.add(new CardBean(R.drawable.p9, "title-9", "desc-9", R.color.colorPrimary));

        rvCard = findViewById(R.id.rv_card);
        adapter = new CardAdapter(this, dataList);
        rvCard.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
//        rvCard.setLayoutManager(new CardLayoutManager());
        rvCard.setAdapter(adapter);
//        rvCard.setItemAnimator(new CustomItemAnimator());
        rvCard.setItemAnimator(new StudyDefaultItemAnimator());
//        rvCard.addItemDecoration(new CustomItemDecoration(this));

        CardItemTouchHelperCallback itemTouchHelperCallback=new CardItemTouchHelperCallback();
        itemTouchHelperCallback.setmListener(new CardItemTouchHelperCallback.OnItemTouchCallbackListener() {
            @Override
            public void onSwiped(int position, int direction) {

                if(dataList!=null){
                    for (int i = 0;i<rvCard.getChildCount();i++){
                        View view = rvCard.getChildAt(i);
                        view.setAlpha(1);
                    }
                    dataList.remove(position);

                    adapter.notifyItemRemoved(position);
                }
            }
        });
        ItemTouchHelper helper=new ItemTouchHelper(itemTouchHelperCallback);
        //将ItemTouchHelper和RecyclerView绑定
        helper.attachToRecyclerView(rvCard);
    }

    public void insert(View view) {
        dataList.add(1,new CardBean(R.drawable.p1, "title-x", "desc-x", R.color.colorAccent));
        adapter.notifyItemInserted(1);
    }

    public void delete(View view) {
        dataList.remove(1);
        adapter.notifyItemRemoved(1);
    }

    public void change(View view) {
        adapter.notifyItemChanged(1);
    }
}
