package com.msaddt.market.product;

import com.msaddt.market.manufacturer.Farmer;

public class Orange extends Fruit {
    public Orange(Farmer farmer) {
        this(farmer, "Orange", 12.0, 7.0);
    }

    public Orange(Farmer farmer, String name, Double price, Double minPrice) {
        super(farmer, name, price, minPrice);
    }
}
