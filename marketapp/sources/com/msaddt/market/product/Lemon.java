package com.msaddt.market.product;

import com.msaddt.market.manufacturer.Farmer;

public class Lemon extends Fruit {
    public Lemon(Farmer farmer) {
        this(farmer, "Lemon", 7.0, 3.0);
    }

    public Lemon(Farmer farmer, String name, Double price, Double minPrice) {
        super(farmer, name, price, minPrice);
    }
}
