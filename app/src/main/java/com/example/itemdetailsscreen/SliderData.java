package com.example.itemdetailsscreen;

public class SliderData {
    private String imageUrl;
    private int imageId;
    public SliderData(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public SliderData(int imageId) {
        this.imageId = imageId;
    }
    public String getImgUrl() {
        return imageUrl;
    }
    public int getImgId() {
        return imageId;
    }
    public void setImgUrl() {
        this.imageUrl = imageUrl;
    }
}
