package com.msaddt.market.product;

import com.msaddt.market.manufacturer.IManufacturer;

public interface IProduct {
    String getName();

    Double getPrice();

    void setPrice(Double price);

    IManufacturer getManufacturer();

    long getTimeCreated();

    Double getMinPrice();
}
