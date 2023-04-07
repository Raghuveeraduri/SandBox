package com.example.sandbox;
public class OrderDataModel {
    String name,product_logo;
    OrderDataModel(){}
    public OrderDataModel(String name,String product_logo) {
        this.name = name;
        this.product_logo=product_logo;
    }

    public String getProduct_logo() {
        return product_logo;
    }

    public void setProduct_logo(String product_logo) {
        this.product_logo = product_logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
