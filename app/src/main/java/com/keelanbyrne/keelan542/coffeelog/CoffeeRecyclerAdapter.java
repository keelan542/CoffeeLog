package com.keelanbyrne.keelan542.coffeelog;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CoffeeRecyclerAdapter extends RecyclerView.Adapter<CoffeeRecyclerAdapter.CustomViewHolder> {

    // Cached copy of coffee logs
    private List<Coffee> coffees;

    private Context context;

    CoffeeRecyclerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        CustomViewHolder customViewHolder = new CustomViewHolder(view);
        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        if (coffees != null) {
            final Coffee coffee = coffees.get(position);
            String extractionString = "";
            switch (coffee.getExtraction()) {
                case 0:
                    extractionString = (context.getResources().getStringArray(R.array.extraction_options))[0];
                    break;
                case 1:
                    extractionString = (context.getResources().getStringArray(R.array.extraction_options))[1];
                    break;
                case 2:
                    extractionString = (context.getResources().getStringArray(R.array.extraction_options))[2];
                    break;
            }

            String methodString = "";
            switch (coffee.getMethod()) {
                case 0:
                    methodString = (context.getResources().getStringArray(R.array.method_options)[0]);
                    holder.methodImage.setImageResource(R.drawable.ic_french_press);
                    break;
                case 1:
                    methodString = (context.getResources().getStringArray(R.array.method_options)[1]);
                    holder.methodImage.setImageResource(R.drawable.ic_aeropress);
                    break;
                case 2:
                    methodString = (context.getResources().getStringArray(R.array.method_options)[2]);
                    holder.methodImage.setImageResource(R.drawable.ic_pour_over);
                    break;
                case 3:
                    methodString = (context.getResources().getStringArray(R.array.method_options)[3]);
                    holder.methodImage.setImageResource(R.drawable.ic_moka_pot);
                    break;
            }

            // Set values on textViews
            holder.method.setText(methodString);
            holder.date.setText(coffee.getDate());
            holder.extraction.setText(extractionString);
            holder.ratio.setText(context.getString(R.string.ratio_text) + coffee.getRatio());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, EditorActivity.class);
                    intent.putExtra(context.getString(R.string.item_clicked_id_extra_tag), coffee);
                    context.startActivity(intent);
                }
            });
        }
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

    class CustomViewHolder extends RecyclerView.ViewHolder {

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
        }
    }
}
