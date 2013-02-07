package com.msaddt.market.product;

import com.msaddt.market.manufacturer.Farmer;

public abstract class Fruit extends AbstractProduct {
    public Fruit(Farmer farmer, String name, Double price, Double minPrice) {
        super(farmer, name, price, minPrice);
    }
}
