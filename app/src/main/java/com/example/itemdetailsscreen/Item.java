package com.example.itemdetailsscreen;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

public class Item  implements Serializable{ //
     int itemID;
     String itemName;
     String ownerEmail;
     String locationCity;
     String description;
     float rateNum;
     String rateUnit;
     float rating;
     float deposit;
     String category;
     int numOfRents;
     int status;
     ArrayList<String> imagesURLs = new ArrayList<>();

}
