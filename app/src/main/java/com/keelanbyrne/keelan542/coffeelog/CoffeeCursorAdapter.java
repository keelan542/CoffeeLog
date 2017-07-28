package com.keelanbyrne.keelan542.coffeelog;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.keelanbyrne.keelan542.coffeelog.data.CoffeeContract.CoffeeEntry;

/**
 * Created by keelan542 on 22/07/2017.
 */

public class CoffeeCursorAdapter extends CursorAdapter {

    public CoffeeCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Get references to required views in list_item.xml
        TextView method = (TextView) view.findViewById(R.id.method);
        TextView date = (TextView) view.findViewById(R.id.date);
        TextView extraction = (TextView) view.findViewById(R.id.extraction);
        TextView ratio = (TextView) view.findViewById(R.id.ratio);
        ImageView methodImage = (ImageView) view.findViewById(R.id.method_image);

        // Get required values from cursor object
        int methodNumber = cursor.getInt(cursor.getColumnIndex(CoffeeEntry.COLUMN_LOG_METHOD));
        String methodString = "";
        String dateString = cursor.getString(cursor.getColumnIndex(CoffeeEntry.COLUMN_LOG_DATE));
        String extractionString = cursor.getString(cursor.getColumnIndex(CoffeeEntry.COLUMN_LOG_EXTRACTION));
        String ratioString = cursor.getString(cursor.getColumnIndex(CoffeeEntry.COLUMN_LOG_RATIO));
        int extractionNumber = Integer.parseInt(extractionString);
        switch (extractionNumber) {
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

        switch (methodNumber) {
            case 0:
                methodString = (context.getResources().getStringArray(R.array.method_options)[0]);
                methodImage.setImageResource(R.drawable.ic_french_press);
                break;
            case 1:
                methodString = (context.getResources().getStringArray(R.array.method_options)[1]);
                methodImage.setImageResource(R.drawable.ic_aeropress);
                break;
            case 2:
                methodString = (context.getResources().getStringArray(R.array.method_options)[2]);
                methodImage.setImageResource(R.drawable.ic_pour_over);
                break;
            case 3:
                methodString = (context.getResources().getStringArray(R.array.method_options)[3]);
                methodImage.setImageResource(R.drawable.ic_moka_pot);
                break;
        }

        // Set values on textViews
        method.setText(methodString);
        date.setText(dateString);
        extraction.setText(extractionString);
        ratio.setText("Ratio - 1:" + ratioString);
    }
}
