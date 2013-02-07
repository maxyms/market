package com.msaddt.market.manufacturer;

import com.msaddt.market.product.IProduct;

public interface IManufacturer {
    IProduct produce();

    String getName();
}