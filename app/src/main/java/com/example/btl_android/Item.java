package com.example.btl_android;

public class Item {
    private String tenBaiHat;
    private String tenCaSi;
    private int itemImg;

    public Item(String tenBaiHat, String tenCaSi, int itemImg) {
        this.tenBaiHat = tenBaiHat;
        this.tenCaSi = tenCaSi;
        this.itemImg = itemImg;
    }

    public String getTenBaiHat() {
        return tenBaiHat;
    }

    public void setTenBaiHat(String tenBaiHat) {
        this.tenBaiHat = tenBaiHat;
    }

    public String getTenCaSi() {
        return tenCaSi;
    }

    public void setTenCaSi(String tenCaSi) {
        this.tenCaSi = tenCaSi;
    }

    public int getItemImg() {
        return itemImg;
    }

    public void setItemImg(int itemImg) {
        this.itemImg = itemImg;
    }

}
