package com.msaddt.market.manufacturer;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.msaddt.market.product.Apple;
import com.msaddt.market.product.Fruit;
import com.msaddt.market.product.IProduct;
import com.msaddt.market.product.Lemon;
import com.msaddt.market.product.Orange;
import com.msaddt.market.product.Watermelon;

public class Farmer implements IManufacturer {
    private String name;
    private Logger logger = LoggerFactory.getLogger(Farmer.class);
    private Set<Fruit> fruits = new HashSet<Fruit>();

    public Farmer(String name) {
        this.name = name;
    }

    @Override
    public IProduct produce() {
        Fruit fruit = null;
        int rnd = (int) Math.round(Math.random() * 3);
        switch (rnd) {
            case 0:
                fruit = new Watermelon(this);
                break;
            case 1:
                fruit = new Apple(this);
                break;
            case 2:
                fruit = new Orange(this);
                break;
            case 3:
                fruit = new Lemon(this);
                break;
            default:
                break;
        }
        logger.debug(this + " produced fruit " + fruit);
        fruits.add(fruit);
        return fruit;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Farmer [" + name + "], fruits produced: " + fruits.size();
    }
}
