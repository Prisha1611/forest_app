package com.example.forest;

import android.net.Uri;

public class RecycledProduct {
    private String productName;
    private String productQuantity;
    private String productPrice;
    private String productLocation;
    private Uri productImage;

    public RecycledProduct(String productName, String productQuantity, String productPrice, String productLocation, Uri productImage) {
        this.productName = productName;
        this.productQuantity = productQuantity;
        this.productPrice = productPrice;
        this.productLocation = productLocation;
        this.productImage = productImage;
    }

    // Getters
    public String getProductName() {
        return productName;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public String getProductLocation() {
        return productLocation;
    }

    public Uri getProductImage() {
        return productImage;
    }

    // Setters can be added if needed
}
