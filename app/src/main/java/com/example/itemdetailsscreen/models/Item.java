package com.example.itemdetailsscreen.models;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

public class Item  implements Serializable{ //
    public int itemID;
    public String itemName;
    public String ownerEmail;
    public String locationCity;
    public String description;
    public float rateNum;
    public String rateUnit;
    public float rating;
    public float deposit;
    public String category;
    public int numOfRents;
    public int status;
    public ArrayList<String> imagesURLs = new ArrayList<>();
//    public Bitmap bitmap;
}