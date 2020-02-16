package com.mulganov.testtask.dp;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Element {
    @PrimaryKey
    @NonNull
    public String text;

//    public void setId(int id){
//        this.id = id;
//    }
//
//    public int getId(){
//        return id;
//    }

    public void setText(String text){
        this.text = text;
    }

    public String getText(){
        return text;
    }

    public String toString(){
        return text;
    }
}
