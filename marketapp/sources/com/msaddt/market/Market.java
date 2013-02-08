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
    private final MarketStatistics statistics = new MarketStatistics();
    private final Queue<IProduct> products = new LinkedList<IProduct>();
    private final Set<IManufacturer> manufacturers = new HashSet<IManufacturer>();
    private final Set<Customer> customers = new HashSet<Customer>();
    private MarketSettings settings;
    private boolean isOpen = false;
    private final static Logger logger = LoggerFactory.getLogger(Market.class);

    public Market(MarketSettings settings) {
        this.settings = settings;
    }

    public MarketStatistics getStatistics() {
        return statistics;
    }

    public void addManufacturer(IManufacturer manufacturer) {
        if (manufacturers.contains(manufacturer)) {
            logger.warn("Manufacturer already on market: " + manufacturer);
        } else {
            manufacturers.add(manufacturer);
            statistics.onManufacturerAdded(manufacturer);
            if (isOpen()) {
                Thread t = new Thread(new ManufacturerProduce(manufacturer));
                t.start();
            }
            logger.debug("Manufacturer added to market: " + manufacturer);
        }
    }

    public void removeManufacturer(IManufacturer manufacturer) {
        if (manufacturers.contains(manufacturer)) {
            manufacturers.remove(manufacturer);
            //            statistics.onManufacturerAdded(manufacturer);
            logger.warn("Manufacturer removed from market: " + manufacturer);
        } else {
            logger.warn("No such manufacturer on market: " + manufacturer);
        }
    }

    public void addCustomer(Customer customer) {
        if (customers.contains(customer)) {
            logger.warn("Customer already on market: " + customer);
        } else {
            customers.add(customer);
            statistics.onCustomerAdded(customer);
            if (isOpen()) {
                Thread t = new Thread(new CustomerPurchase(customer));
                t.start();
            }
            logger.debug("Customer added to market: " + customer);
        }
    }

    public void removeCustomer(Customer customer) {
        if (customers.contains(customer)) {
            customers.remove(customer);
            //            statistics.onManufacturerAdded(manufacturer);
            logger.warn("Customer removed from market: " + customer);
        } else {
            logger.warn("No such customer on market: " + customer);
        }
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
        logger.info(toString());
    }

    private class ManufacturerProduce implements Runnable {
        private IManufacturer manufacturer;

        public ManufacturerProduce(IManufacturer manufacturer) {
            this.manufacturer = manufacturer;
        }

        @Override
        public void run() {
            while (isOpen && manufacturers.contains(manufacturer)) {
                IProduct product = manufacturer.produce();
                if (product != null) {
                    synchronized (products) {
                        if (isOpen) {
                            getProducts().add(product);
                            logger.debug(manufacturer.getName() + " added product " + product + " to market ");
                            statistics.onProductAdded(product);
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
                                    statistics.onProductDischarged(product);
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

    public Queue<IProduct> getProducts() {
        return products;
    }

    public Set<IManufacturer> getManufacturers() {
        return manufacturers;
    }

    public Set<Customer> getCustomers() {
        return customers;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int totalProductsBought = 0;
        double totalInitialCustomerBalance = 0;
        for (Customer customer : getCustomers()) {
            totalProductsBought += customer.getProducts().size();
            totalInitialCustomerBalance += customer.getInitialBalance();
        }
        int totalProductsProduced = 0;
        for (IManufacturer manufacturer : getManufacturers()) {
            totalProductsProduced += manufacturer.getProducts().size();
        }
        sb.append("-------------------Market statistics----------------");
        sb.append(String.format("\nTotal initial customers balance: [$%.2f]", totalInitialCustomerBalance));
        sb.append("\n----Products:");
        sb.append("\nTotal Products produced: " + totalProductsProduced);
        sb.append("\nTotal Products appeared on market: " + statistics.getProducts().size());
        sb.append("\nTotal Products bought by customers: " + totalProductsBought);
        sb.append("\nTotal Products discharged: " + statistics.getDischargedProducts().size());
        sb.append("\nTotal Products remained: " + getProducts().size());
        sb.append("\nProducts Remain: " + getProducts());
        sb.append("\n");
        sb.append("\nProducts Discharged: ] " + statistics.getDischargedProducts());
        sb.append("\n");
        sb.append("\n----Manufacturers:");
        for (IManufacturer manufacturer : getManufacturers()) {
            sb.append("\n  " + manufacturer);
        }
        sb.append("\n");
        sb.append("\n----Customers:");
        for (Customer customer : getCustomers()) {
            sb.append("\n  " + customer);
        }
        return sb.toString();
    }

    public boolean isOpen() {
        return isOpen;
    }
}
