package com.msaddt.market.product;

import com.msaddt.market.manufacturer.IManufacturer;

public abstract class AbstractProduct implements IProduct {
    private String name;
    private Double price;
    private IManufacturer manufacturer;

    public AbstractProduct(IManufacturer manufacturer, String name, Double price) {
        this.manufacturer = manufacturer;
        this.name = name;
        this.price = price;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Double getPrice() {
        return price;
    }

    @Override
    public IManufacturer getManufacturer() {
        return manufacturer;
    }

    @Override
    public String toString() {
        return getName() + " [" + getPrice() + "] produced by " + getManufacturer().getName();
    }

    protected void setName(String name) {
        this.name = name;
    }

    @Override
    public void setPrice(Double price) {
        this.price = price;
    }

    protected void setManufacturer(IManufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }
}
