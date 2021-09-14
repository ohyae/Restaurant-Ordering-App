package com.example.madfinal;

public class Product {
    private  int id;
    private String title;
    private float price;

    public Product() {
        id = 0;
        title = "Not Set";
        price = 0;

    }

    public Product(int i, String t, float p) {
        id = i;
        title = t;
        price = p;

    }

    public void setId(int v) { id = v; }
    public void setTitle(String v) { title = v; }
    public void setPrice(float v) { price = v; }


    public int getId() { return id; }
    public String getTitle() { return title; }
    public float getPrice() { return price; }

}
