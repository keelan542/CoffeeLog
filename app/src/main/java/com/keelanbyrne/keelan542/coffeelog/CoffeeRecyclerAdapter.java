package com.keelanbyrne.keelan542.coffeelog;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.keelanbyrne.keelan542.coffeelog.data.CoffeeContract.CoffeeEntry;

/**
 * Created by keelan542 on 16/08/2017.
 */

public class CoffeeRecyclerAdapter extends RecyclerView.Adapter<CoffeeRecyclerAdapter.CustomViewHolder> {

    // Fields
    private Context mContext;
    private Cursor mCursor;
    private OnItemClickListener mItemClickListener;

    public interface OnItemClickListener {
        void onListItemClick(View view, int position);
    }

    CoffeeRecyclerAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
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

        // Move cursor to position in holder and return
        // early if null
        if (!mCursor.moveToPosition(position))
            return;

        // Get required values from cursor object
        int methodNumber = mCursor.getInt(mCursor.getColumnIndex(CoffeeEntry.COLUMN_LOG_METHOD));
        String methodString = "";
        String dateString = mCursor.getString(mCursor.getColumnIndex(CoffeeEntry.COLUMN_LOG_DATE));
        String extractionString = mCursor.getString(mCursor.getColumnIndex(CoffeeEntry.COLUMN_LOG_EXTRACTION));
        String ratioString = mCursor.getString(mCursor.getColumnIndex(CoffeeEntry.COLUMN_LOG_RATIO));
        int extractionNumber = Integer.parseInt(extractionString);
        switch (extractionNumber) {
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

        switch (methodNumber) {
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
        holder.date.setText(dateString);
        holder.extraction.setText(extractionString);
        holder.ratio.setText("Ratio - 1:" + ratioString);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }

        mCursor = cursor;

        if (mCursor != null) {
            this.notifyDataSetChanged();
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
