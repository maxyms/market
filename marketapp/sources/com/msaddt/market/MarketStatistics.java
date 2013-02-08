package com.msaddt.market;

import java.util.HashSet;
import java.util.Set;

import com.msaddt.market.manufacturer.IManufacturer;
import com.msaddt.market.product.IProduct;

public class MarketStatistics {
    private final Set<IProduct> dischargedProducts = new HashSet<IProduct>();
    private final Set<IProduct> products = new HashSet<IProduct>();
    private final Set<IManufacturer> manufacturers = new HashSet<IManufacturer>();
    private final Set<Customer> customers = new HashSet<Customer>();

    public void onProductAdded(IProduct product) {
        products.add(product);
    }

    public void onProductDischarged(IProduct product) {
        dischargedProducts.add(product);
    }

    public void onManufacturerAdded(IManufacturer manufacturer) {
        manufacturers.add(manufacturer);
    }

    public void onCustomerAdded(Customer customer) {
        customers.add(customer);
    }

    public Set<IProduct> getProducts() {
        return products;
    }

    public Set<IProduct> getDischargedProducts() {
        return dischargedProducts;
    }

    public Set<IManufacturer> getManufacturers() {
        return manufacturers;
    }

    public Set<Customer> getCustomers() {
        return customers;
    }

    public Set<IProduct> getProducedProducts() {
        Set<IProduct> producedProducts = new HashSet<IProduct>();
        for (IManufacturer manufacturer : getManufacturers()) {
            producedProducts.addAll(manufacturer.getProducts());
        }
        return producedProducts;
    }

    public Set<IProduct> getBoughtProducts() {
        Set<IProduct> boughtProducts = new HashSet<IProduct>();
        for (Customer customer : getCustomers()) {
            boughtProducts.addAll(customer.getProducts());
        }
        return boughtProducts;
    }

    public Double getInitialCustomersBalance() {
        Double totalInitialCustomerBalance = 0.0;
        for (Customer customer : getCustomers()) {
            totalInitialCustomerBalance += customer.getInitialBalance();
        }
        return totalInitialCustomerBalance;
    }
}
