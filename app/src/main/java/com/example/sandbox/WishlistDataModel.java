package com.example.sandbox;

public class WishlistDataModel {
    String Name;String product_logo;long discount;long rate;
    WishlistDataModel(){}

    public WishlistDataModel(String Name, String product_logo, long discount, long rate) {
        this.Name = Name;this.rate=rate;
        this.product_logo=product_logo;this.discount=discount;
    }

    public String getProduct_logo() {
        return product_logo;
    }

    public void setProduct_logo(String product_logo) {
        this.product_logo = product_logo;
    }

    public long getDiscount() {
        return discount;
    }

    public void setDiscount(long discount) {
        this.discount = discount;
    }

    public long getRate() {
        return rate;
    }

    public void setRate(long rate) {
        this.rate = rate;
    }

    public String getName() {
        return Name;
    }

    public void setName(String game) {
        Name = Name;
    }
}
