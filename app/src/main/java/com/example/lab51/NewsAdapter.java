package com.example.lab51;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private static List<NewsItem> newsItemList;

    public NewsAdapter(List<NewsItem> newsItemList) {
        this.newsItemList = newsItemList;
    }
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View newsView = inflater.inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(newsView);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsItem newsItem = newsItemList.get(position);
        holder.txtTitle.setText(newsItem.getTitle());
        if (newsItem.getImageUrl().startsWith("/")) {
            File imageFile = new File(newsItem.getImageUrl());
            Glide.with(holder.itemView.getContext())
                    .load(imageFile)
                    .apply(new RequestOptions()
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .centerCrop()
                    )
                    .into(holder.imgNew);
        } else {
            // Load the image using Glide from the URL and set it into the ImageView
            Glide.with(holder.itemView.getContext())
                    .load(newsItem.getImageUrl())
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(holder.imgNew);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });
    }
    public void updateDataSet(List<NewsItem> newDataSet) {
        this.newsItemList = newDataSet;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return newsItemList.size();
    }

    public int deleteItem(int pos) {
        NewsItem newsItem = newsItemList.get(pos);
        newsItemList.remove(newsItem);
        return newsItem.getId();
    }
    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgNew;
        private TextView txtTitle;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            imgNew = itemView.findViewById(R.id.imageNews);
            txtTitle = itemView.findViewById(R.id.txtTitle);
        }
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
