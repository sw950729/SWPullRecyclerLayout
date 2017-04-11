package com.angel.adapter;

import android.support.v7.util.DiffUtil;

import java.util.List;

/**
 * Created by Angel on 2017/4/10.
 */
public class SWDiffCallBack<T> extends DiffUtil.Callback {

    private List<T> olddatas;
    private List<T> newDatas;

    public SWDiffCallBack(List<T> olddatas, List<T> newDatas) {
        this.olddatas = olddatas;
        this.newDatas = newDatas;
    }

    public int getOldListSize() {
        return olddatas.size();
    }

    public int getNewListSize() {
        return newDatas.size();
    }

    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return olddatas.get(oldItemPosition).equals(newDatas.get(newItemPosition));
    }

    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return olddatas.get(oldItemPosition).equals(newDatas.get(newItemPosition));
    }
}
