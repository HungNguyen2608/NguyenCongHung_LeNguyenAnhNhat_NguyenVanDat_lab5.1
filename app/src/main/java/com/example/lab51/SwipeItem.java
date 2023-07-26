package com.example.lab51;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;


public class SwipeItem extends ItemTouchHelper.SimpleCallback {
   NewsAdapter mAdapter;
    Context context;
    public SwipeItem(NewsAdapter adapter, Context context) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        mAdapter = adapter;
        this.context = context;
    }
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int pos = viewHolder.getAdapterPosition();
        int id = this.mAdapter.deleteItem(pos);
        NewsItem.DSNews().remove(id);
        mAdapter.notifyItemRemoved(pos);
        mAdapter.notifyItemRangeChanged(pos, mAdapter.getItemCount());
    }
}