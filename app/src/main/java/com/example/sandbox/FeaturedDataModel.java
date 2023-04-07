package com.example.sandbox;

public class FeaturedDataModel {
    long discount;long rate;String  product_logo;String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    FeaturedDataModel(){}
    public FeaturedDataModel(long discount,long rate,String product_logo,String name) {
        this.discount = discount;
        this.rate = rate;
        this.product_logo=product_logo;
        this.name=name;
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

    public String getProduct_logo() {
        return product_logo;
    }

    public void setProduct_logo(String product_logo) {
        this.product_logo = product_logo;
    }
}
