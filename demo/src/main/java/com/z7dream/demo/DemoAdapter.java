package com.z7dream.demo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Z7Dream on 2017/8/16 16:04.
 * Email:zhangxyfs@126.com
 */

public class DemoAdapter extends RecyclerView.Adapter<DemoHolder> {
    public List<Model> dataList;

    public DemoAdapter() {
        dataList = new ArrayList<>();
    }

    public void setDataList(List<Model> list, boolean isRef) {
        if (isRef)
            dataList.clear();
        dataList.addAll(list);
    }

    @Override
    public DemoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DemoHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, null));
    }

    @Override
    public void onBindViewHolder(DemoHolder holder, int position) {
        holder.textView.setText(dataList.get(position).text);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class Model {
        public String text;
    }
}
