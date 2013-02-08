package com.msaddt.market.manufacturer;

import java.util.Set;

import com.msaddt.market.product.IProduct;

public interface IManufacturer {
    IProduct produce();

    String getName();

    Set<IProduct> getProducts();

    void notifyProductLongTerm(IProduct product);
}