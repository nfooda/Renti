package com.example.itemdetailsscreen;

import android.graphics.Bitmap;

public class SliderData {
    private String imageUrl;
    private int imageId;
    private Bitmap bitmap;
    public SliderData(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public SliderData(int imageId) {
        this.imageId = imageId;
    }
    public SliderData(Bitmap bitmap) { this.bitmap = bitmap;}
    public String getImgUrl() {
        return imageUrl;
    }
    public int getImgId() {
        return imageId;
    }
    public Bitmap getImgBitmap() {return bitmap;}
    public void setImgUrl() {
        this.imageUrl = imageUrl;
    }
}
