package com.example.sandbox;

public class PaymentDataModel {
    String name;String product_logo;long discount;long rate;
    PaymentDataModel(){}
    public PaymentDataModel(String name, String product_logo, long discount, long rate) {
        name = name;
        this.product_logo = product_logo;
        this.discount = discount;
        this.rate = rate;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
