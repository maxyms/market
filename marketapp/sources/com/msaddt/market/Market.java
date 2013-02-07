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
    private int totalProducts;
    private final Queue<IProduct> products = new LinkedList<IProduct>();
    private final Set<IProduct> dischargedProducts = new HashSet<IProduct>();
    private final Set<IManufacturer> manufacturers = new HashSet<IManufacturer>();
    private final Set<Customer> customers = new HashSet<Customer>();
    private MarketSettings settings;
    boolean isOpen = false;
    private final static Logger logger = LoggerFactory.getLogger(Market.class);

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
        logger.info("Market initialization starts...");
        for (IManufacturer manufacturer : manufacturers) {
            Thread t = new Thread(new ManufacturerProduce(manufacturer));
            t.start();
        }
        for (Customer customer : customers) {
            Thread t = new Thread(new CustomerPurchase(customer));
            t.start();
        }
        Thread t = new Thread(new LongTermProductMonitor());
        t.start();
        logger.info("Market initialization ends...");
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
                    synchronized (products) {
                        if (isOpen) {
                            getProducts().add(fruit);
                            logger.debug(manufacturer.getName() + " added fruit " + fruit + " to market ");
                            totalProducts++;
                        }
                    }
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
                    if (isOpen) {
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
                }
                try {
                    Thread.sleep(settings.getCustomerBuyPeriod());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private class LongTermProductMonitor implements Runnable {
        @Override
        public void run() {
            while (isOpen) {
                boolean areProductsExist = false;
                synchronized (products) {
                    if (isOpen) {
                        areProductsExist = !getProducts().isEmpty();
                        if (areProductsExist) {
                            logger.debug("Perform long-term check...");
                            long currentTime = System.currentTimeMillis();
                            Set<IProduct> productsSnapshot = new HashSet<IProduct>(getProducts());
                            for (IProduct product : productsSnapshot) {
                                long timeOnMarket = currentTime - product.getTimeCreated();
                                if (timeOnMarket > settings.getMaxTimeOnMarket()) {
                                    getProducts().remove(product);
                                    dischargedProducts.add(product);
                                    logger.warn("Product [" + product + " was removed from market, long-term is " + timeOnMarket);
                                } else if (timeOnMarket > settings.getLongTermTimeOnMarket()) {
                                    product.getManufacturer().notifyProductLongTerm(product);
                                }
                            }
                        }
                    }
                }
                try {
                    logger.debug("Waiting long-term monitor...");
                    Thread.sleep(settings.getLongTermMonitorFrequency());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private Queue<IProduct> getProducts() {
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Total Products appeared on market: " + getTotalProducts());
        synchronized (products) {
            sb.append("\nProducts Discharged: [" + dischargedProducts.size() + "] " + dischargedProducts);
            sb.append("\nProducts Remain: [" + getProducts().size() + "] " + getProducts());
        }
        return sb.toString();
    }
}
