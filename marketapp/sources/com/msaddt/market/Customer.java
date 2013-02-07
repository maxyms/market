package com.msaddt.market;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.msaddt.market.product.IProduct;

public class Customer {
    private String name;
    private Double balance;
    private Set<IProduct> products = new HashSet<IProduct>();
    private Logger logger = LoggerFactory.getLogger(Customer.class);

    public Customer(String name, Double balance) {
        this.name = name;
        this.balance = balance;
    }

    public boolean buy(IProduct product) {
        boolean isBought = false;
        if (balance >= product.getPrice()) {
            balance -= product.getPrice();
            products.add(product);
            isBought = true;
        } else {
            logger.debug(this.getName() + " has no enough money to buy the product " + product);
        }
        return isBought;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("Customer [%s], Balance [$%.2f], Products#: [%d], Products: %s", name, balance, products.size(),
                products.toString());
    }
}
