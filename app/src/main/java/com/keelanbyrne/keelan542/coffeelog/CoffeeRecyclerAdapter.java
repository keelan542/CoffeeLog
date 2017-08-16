package com.keelanbyrne.keelan542.coffeelog;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by keelan542 on 16/08/2017.
 */

public class CoffeeRecyclerAdapter extends RecyclerView.Adapter<CoffeeRecyclerAdapter.CustomViewHolder> {

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        CustomViewHolder(View itemView) {
            super(itemView);
        }
    }
}
