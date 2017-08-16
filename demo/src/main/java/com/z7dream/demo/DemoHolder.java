package com.z7dream.demo;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Z7Dream on 2017/8/16 16:04.
 * Email:zhangxyfs@126.com
 */

public class DemoHolder extends RecyclerView.ViewHolder {
    public TextView textView;

    public DemoHolder(View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.text);
    }
}
