package com.msaddt.market.product;

import com.msaddt.market.manufacturer.IManufacturer;

public abstract class AbstractProduct implements IProduct {
    private String name;
    private Double price;
    private Double minPrice;
    private IManufacturer manufacturer;
    private long timeCreated;
    private boolean onSale = false;

    public AbstractProduct(IManufacturer manufacturer, String name, Double price, Double minPrice) {
        this.manufacturer = manufacturer;
        this.name = name;
        this.price = price;
        this.minPrice = minPrice;
        setTimeCreated();
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
    public Double getMinPrice() {
        return minPrice;
    }

    @Override
    public IManufacturer getManufacturer() {
        return manufacturer;
    }

    @Override
    public String toString() {
        return (onSale ? "ON SALE, " : "") + getName() + " [" + getPrice() + "] produced by " + getManufacturer().getName();
    }

    protected void setName(String name) {
        this.name = name;
    }

    @Override
    public void setPrice(Double price) {
        if (price < this.price) {
            onSale = true;
        }
        this.price = price;
    }

    protected void setManufacturer(IManufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Override
    public long getTimeCreated() {
        return timeCreated;
    }

    private void setTimeCreated() {
        timeCreated = System.currentTimeMillis();
    }
}
