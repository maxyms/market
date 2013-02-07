package com.msaddt.market.product;

import com.msaddt.market.manufacturer.Farmer;

public class Watermelon extends Fruit {
    public Watermelon(Farmer farmer) {
        this(farmer, "Watermelon", 20.0);
    }

    public Watermelon(Farmer farmer, String name, Double price) {
        super(farmer, name, price);
    }
}
