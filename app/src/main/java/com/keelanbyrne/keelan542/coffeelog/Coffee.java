package com.keelanbyrne.keelan542.coffeelog;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "coffee_log")
public class Coffee {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "_id")
    private int id;

    private int method;

    @NonNull
    @ColumnInfo(name = "coffee_amount")
    private String coffeeUsed;

    @NonNull
    private String yield;

    @NonNull
    private String ratio;

    @NonNull
    private String time;

    @NonNull
    private int extraction;

    @NonNull
    private String date;

    @NonNull
    private String comment;

    public Coffee(int method, @NonNull String coffeeUsed, @NonNull String yield, @NonNull String ratio, @NonNull String time, @NonNull int extraction, @NonNull String date, @NonNull String comment) {
        this.method = method;
        this.coffeeUsed = coffeeUsed;
        this.yield = yield;
        this.ratio = ratio;
        this.time = time;
        this.extraction = extraction;
        this.date = date;
        this.comment = comment;
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    @NonNull
    public String getCoffeeUsed() {
        return coffeeUsed;
    }

    public void setCoffeeUsed(@NonNull String coffeeUsed) {
        this.coffeeUsed = coffeeUsed;
    }

    @NonNull
    public String getYield() {
        return yield;
    }

    public void setYield(@NonNull String yield) {
        this.yield = yield;
    }

    @NonNull
    public String getRatio() {
        return ratio;
    }

    public void setRatio(@NonNull String ratio) {
        this.ratio = ratio;
    }

    @NonNull
    public String getTime() {
        return time;
    }

    public void setTime(@NonNull String time) {
        this.time = time;
    }

    @NonNull
    public int getExtraction() {
        return extraction;
    }

    public void setExtraction(@NonNull int extraction) {
        this.extraction = extraction;
    }

    @NonNull
    public String getDate() {
        return date;
    }

    public void setDate(@NonNull String date) {
        this.date = date;
    }

    @NonNull
    public String getComment() {
        return comment;
    }

    public void setComment(@NonNull String comment) {
        this.comment = comment;
    }
}
