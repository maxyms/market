package com.msaddt.market.product;

import com.msaddt.market.manufacturer.Farmer;

public class Apple extends Fruit {
    public Apple(Farmer farmer) {
        this(farmer, "Apple", 10.0);
    }

    public Apple(Farmer farmer, String name, Double price) {
        super(farmer, name, price);
    }
}
