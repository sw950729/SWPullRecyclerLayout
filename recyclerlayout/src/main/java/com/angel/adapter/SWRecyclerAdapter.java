package com.angel.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Angel on 2017/3/28.
 */
public abstract class SWRecyclerAdapter<T> extends RecyclerView.Adapter<SWViewHolder> {

    private List<T> list;
    private LayoutInflater inflater;

    public SWRecyclerAdapter(Context context, List<T> list) {
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    public SWViewHolder onCreateViewHolder(ViewGroup parent, int layoutID) {
        final SWViewHolder holder = new SWViewHolder(
                inflater.inflate(getItemLayoutId(layoutID), parent, false));
        return holder;
    }

    public void onBindViewHolder(SWViewHolder holder, int position) {
        bindData(holder, position, list.get(position));
    }

    abstract public int getItemLayoutId(int layoutID);

    abstract public void bindData(SWViewHolder holder, int position, T item);

    public int getItemCount() {
        return list.size();
    }

    public void add(int position, T item) {
        list.add(position, item);
        notifyItemInserted(position);
    }

    public void delete(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    public void clear() {
        if (list != null && list.size() > 0) {
            list.clear();
        }
        notifyDataSetChanged();
    }

    public void setTop(int position) {
        list.add(0, list.get(position));
        list.remove(position + 1);
        notifyDataSetChanged();
    }

}
