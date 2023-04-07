package com.example.sandbox;

public class CategoryGameDataModel {
    long discount;long rate;String  product_logo;
    CategoryGameDataModel(){}
    public CategoryGameDataModel(long discount,long rate,String product_logo) {
        this.discount = discount;
        this.rate = rate;
        this.product_logo=product_logo;
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
