package com.msaddt.market;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.msaddt.market.manufacturer.Farmer;
import com.msaddt.market.manufacturer.IManufacturer;

public class TestMarket {
    private static final Logger logger = LoggerFactory.getLogger(TestMarket.class);

    public static void main(String... strings) throws InterruptedException {
        logger.debug("-----------Start Market Aplication-----------");
        Market market = new Market();
        int farmers = 3;
        for (int i = 0; i < farmers; i++) {
            Farmer farmer = new Farmer("Farmer " + (i + 1));
            market.addManufacturer(farmer);
        }
        int customers = 5;
        for (int i = 0; i < customers; i++) {
            Double balance = Math.ceil(Math.random() * 100);
            Customer customer = new Customer("Customer " + (i + 1), balance);
            market.addCustomer(customer);
        }
        market.open();
        Thread.sleep(10000);
        market.close();
        for (Customer customer : market.getCustomers()) {
            logger.debug(customer.toString());
        }
        for (IManufacturer farmer : market.getManufacturers()) {
            logger.debug(farmer.toString());
        }
        logger.debug("Total Products: " + market.getTotalProducts());
        logger.debug("Products Remain: " + market.getProducts());
        logger.debug("-----------End Market Aplication-----------");
    }
}
