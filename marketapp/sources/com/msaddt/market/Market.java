package com.msaddt.market;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.msaddt.market.manufacturer.IManufacturer;
import com.msaddt.market.product.IProduct;

public class Market {
    int totalProducts;
    private Queue<IProduct> products = new LinkedList<IProduct>();
    Set<IManufacturer> manufacturers = new HashSet<IManufacturer>();
    private Set<Customer> customers = new HashSet<Customer>();
    MarketSettings settings;
    private static final MarketSettings DEFAULT_SETTINGS = new MarketSettings() {
        @Override
        public long getCustomerBuyPeriod() {
            return 500;
        }

        @Override
        public long getFarmerProducePeriod() {
            return 1000;
        }
    };
    boolean isOpen = false;
    Logger logger = LoggerFactory.getLogger(Market.class);

    public Market() {
        this.settings = DEFAULT_SETTINGS;
    }

    public Market(MarketSettings settings) {
        this.settings = settings;
    }

    public void addManufacturer(IManufacturer manufacturer) {
        manufacturers.add(manufacturer);
        logger.debug("Manufacturer added to market: " + manufacturer);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
        logger.debug("Customer added to market: " + customer);
    }

    public void open() {
        isOpen = true;
        logger.info("Market opened...");
        for (IManufacturer manufacturer : manufacturers) {
            Thread t = new Thread(new ManufacturerProduce(manufacturer));
            t.start();
        }
        for (Customer customer : customers) {
            Thread t = new Thread(new CustomerPurchase(customer));
            t.start();
        }
    }

    public void close() {
        isOpen = false;
        logger.info("Market closed...");
    }

    private class ManufacturerProduce implements Runnable {
        private IManufacturer manufacturer;

        public ManufacturerProduce(IManufacturer manufacturer) {
            this.manufacturer = manufacturer;
        }

        @Override
        public void run() {
            while (isOpen && manufacturers.contains(manufacturer)) {
                IProduct fruit = manufacturer.produce();
                if (fruit != null) {
                    synchronized (fruit) {
                        getProducts().add(fruit);
                        logger.debug(manufacturer.getName() + " added fruit " + fruit + " to market ");
                    }
                    totalProducts++;
                }
                try {
                    Thread.sleep(settings.getFarmerProducePeriod());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private class CustomerPurchase implements Runnable {
        private Customer customer;

        private CustomerPurchase(Customer customer) {
            this.customer = customer;
        }

        @Override
        public void run() {
            while (isOpen) {
                boolean areFruitsExists = false;
                synchronized (products) {
                    areFruitsExists = !getProducts().isEmpty();
                    if (areFruitsExists) {
                        IProduct fruit = getProducts().peek();
                        if (customer.buy(fruit)) {
                            getProducts().remove(fruit);
                            logger.debug(customer.getName() + " bought product " + fruit);
                        }
                    } else {
                        logger.debug(customer.getName() + " is waiting for fruits...");
                    }
                }
                try {
                    Thread.sleep(settings.getCustomerBuyPeriod());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public Queue<IProduct> getProducts() {
        return products;
    }

    public int getTotalProducts() {
        return totalProducts;
    }

    public Set<IManufacturer> getManufacturers() {
        return manufacturers;
    }

    public Set<Customer> getCustomers() {
        return customers;
    }
}
