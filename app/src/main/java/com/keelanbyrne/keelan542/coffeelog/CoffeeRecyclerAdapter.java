package com.keelanbyrne.keelan542.coffeelog;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by keelan542 on 16/08/2017.
 */

public class CoffeeRecyclerAdapter extends RecyclerView.Adapter<CoffeeRecyclerAdapter.CustomViewHolder> {

    private List<Coffee> coffees; // cached copy of coffee logs

    // Fields
    private Context mContext;
    private OnItemClickListener mItemClickListener;

    public interface OnItemClickListener {
        void onListItemClick(View view, int position);
    }

    CoffeeRecyclerAdapter(Context context) {
        mContext = context;
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
        CustomViewHolder customViewHolder = new CustomViewHolder(view);
        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        Coffee coffee = coffees.get(position);
        String extractionString = "";
        switch (coffee.getExtraction()) {
            case 0:
                extractionString = (mContext.getResources().getStringArray(R.array.extraction_options))[0];
                break;
            case 1:
                extractionString = (mContext.getResources().getStringArray(R.array.extraction_options))[1];
                break;
            case 2:
                extractionString = (mContext.getResources().getStringArray(R.array.extraction_options))[2];
                break;
        }

        String methodString = "";
        switch (coffee.getMethod()) {
            case 0:
                methodString = (mContext.getResources().getStringArray(R.array.method_options)[0]);
                holder.methodImage.setImageResource(R.drawable.ic_french_press);
                break;
            case 1:
                methodString = (mContext.getResources().getStringArray(R.array.method_options)[1]);
                holder.methodImage.setImageResource(R.drawable.ic_aeropress);
                break;
            case 2:
                methodString = (mContext.getResources().getStringArray(R.array.method_options)[2]);
                holder.methodImage.setImageResource(R.drawable.ic_pour_over);
                break;
            case 3:
                methodString = (mContext.getResources().getStringArray(R.array.method_options)[3]);
                holder.methodImage.setImageResource(R.drawable.ic_moka_pot);
                break;
        }

        // Set values on textViews
        holder.method.setText(methodString);
        holder.date.setText(coffee.getDate());
        holder.extraction.setText(extractionString);
        holder.ratio.setText(mContext.getString(R.string.ratio_text) + coffee.getRatio());
    }

    void setCoffees(List<Coffee> coffees) {
        this.coffees = coffees;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (coffees != null) {
            return coffees.size();
        } else {
            return 0;
        }
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Get references to views of list item
        private TextView method;
        private TextView date;
        private TextView extraction;
        private TextView ratio;
        private ImageView methodImage;

        public CustomViewHolder(View itemView) {
            super(itemView);

            // Get references to required views in list_item.xml
            method = (TextView) itemView.findViewById(R.id.method);
            date = (TextView) itemView.findViewById(R.id.date);
            extraction = (TextView) itemView.findViewById(R.id.extraction);
            ratio = (TextView) itemView.findViewById(R.id.ratio);
            methodImage = (ImageView) itemView.findViewById(R.id.method_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mItemClickListener.onListItemClick(v, getAdapterPosition());
        }
    }
}
