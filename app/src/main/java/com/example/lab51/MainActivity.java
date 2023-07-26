package com.example.lab51;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<NewsItem> newsItemList;
    private NewsAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView rvNews = findViewById(R.id.rcvNews);
        newsItemList = NewsItem.DSNews();
        newsAdapter= new NewsAdapter(newsItemList);
        rvNews.setAdapter(newsAdapter);
        rvNews.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvNews.addItemDecoration(itemDecoration);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeItem(newsAdapter, this));
        itemTouchHelper.attachToRecyclerView(rvNews);
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                addActivityLauncher.launch(intent);
            }
        });
        newsAdapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
            public void onItemClick(int position) {
                NewsItem clickedItem = newsItemList.get(position);
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("url", clickedItem.getUrl());
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            String title = data.getStringExtra("Title");
            String imgURL = data.getStringExtra("ImgURL");
            String url = data.getStringExtra("URL");
            NewsItem newItem = new NewsItem(imgURL,title, url);
            newsItemList.add(newItem);
            newsAdapter.notifyDataSetChanged();
        }
    }
    private ActivityResultLauncher<Intent> addActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String title = result.getData().getStringExtra("Title");
                    String imgURL = result.getData().getStringExtra("ImgURL");
                    String url = result.getData().getStringExtra("URL");
                    NewsItem newItem = new NewsItem(title, imgURL, url);
                    newsItemList.add(newItem);
                    newsAdapter.notifyDataSetChanged();
                }
            }
    );
}