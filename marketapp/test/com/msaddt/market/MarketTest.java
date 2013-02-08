package com.msaddt.market;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.msaddt.market.manufacturer.Farmer;
import com.msaddt.market.manufacturer.IManufacturer;
import com.msaddt.market.product.IProduct;

@RunWith(value = Parameterized.class)
public class MarketTest {
    private Integer manufacturers;
    private Integer customers;

    @Parameters
    public static Collection<Integer[]> getTestParameters() {
        return Arrays.asList(new Integer[][] { { 3, 10 } /*, { 1, 10 }, { 5, 5 }, { 10, 1 }*/});
    }

    public MarketTest(Integer manufacturers, Integer customers) {
        this.manufacturers = manufacturers;
        this.customers = customers;
    }

    @Test
    @Ignore
    public void testMarket() throws InterruptedException {
        Market market = initMarket();
        market.open();
        Thread.sleep(10000);
        market.close();
        validateMarket(market);
    }

    @Test
    @Ignore
    public void testMarketAddManufacturer() throws InterruptedException {
        Market market = initMarket();
        market.open();
        Thread.sleep(5000);
        IManufacturer newManufacturer = new Farmer("Farmer " + (manufacturers + 1));
        market.addManufacturer(newManufacturer);
        Thread.sleep(2000);
        market.removeManufacturer(new Farmer("Farmer " + (manufacturers + 2)));
        Thread.sleep(2000);
        market.removeManufacturer(newManufacturer);
        Thread.sleep(1000);
        market.close();
        validateMarket(market);
    }

    @Test
    public void testMarketAddCustomer() throws InterruptedException {
        Market market = initMarket();
        market.open();
        Thread.sleep(2000);
        Customer newCustomer = new Customer("Customer " + (customers + 1), Math.ceil(Math.random() * 100));
        market.addCustomer(newCustomer);
        Thread.sleep(1000);
        market.removeCustomer(new Customer("Customer " + (customers + 2), Math.ceil(Math.random() * 100)));
        Thread.sleep(6000);
        market.removeCustomer(newCustomer);
        Thread.sleep(1000);
        market.close();
        validateMarket(market);
    }

    private Market initMarket() {
        final MarketSettings settings = getSettings();
        Market market = new Market(settings);
        for (int i = 0; i < manufacturers; i++) {
            IManufacturer manufacturer = new Farmer("Farmer " + (i + 1));
            market.addManufacturer(manufacturer);
        }
        for (int i = 0; i < customers; i++) {
            Double balance = Math.ceil(Math.random() * 100);
            Customer customer = new Customer("Customer " + (i + 1), balance);
            market.addCustomer(customer);
        }
        return market;
    }

    private MarketSettings getSettings() {
        final MarketSettings settings = new MarketSettings();
        settings.setCustomerBuyPeriod(500);
        settings.setFarmerProducePeriod(1000);
        settings.setLongTermMonitorFrequency(100);
        settings.setMaxTimeOnMarket(2000);
        settings.setLongTermTimeOnMarket(1000);
        return settings;
    }

    private void validateMarket(Market market) {
        assertFalse("Market is open", market.isOpen());
        MarketStatistics statistics = market.getStatistics();
        assertNotNull("Market statistics", statistics);
        //        assertEquals("Manufacturers were on market", manufacturers.intValue() , statistics.getManufacturers().size());
        //        assertEquals("Customers were on market", customers.intValue(), statistics.getCustomers().size());
        assertTrue("Produced products not less those were on market",
                statistics.getProducedProducts().size() >= statistics.getProducts().size());
        assertTrue("Bought products not more those were on market",
                statistics.getBoughtProducts().size() <= statistics.getProducts().size());
        assertTrue("Initial customers balance not less 0.0", statistics.getInitialCustomersBalance() >= 0);
        assertTrue("Discharged products not more those were on market",
                statistics.getDischargedProducts().size() <= statistics.getProducts().size());
        assertTrue("Remained products not more those were on market", market.getProducts().size() <= statistics.getProducts().size());
        assertEquals("Products on market match to bought, discharged and remained", statistics.getProducts().size(),
                market.getProducts().size() + statistics.getBoughtProducts().size() + statistics.getDischargedProducts().size());
        validateDischargedProducts(market);
        validateCustomers(market);
    }

    private void validateDischargedProducts(Market market) {
        MarketStatistics statistics = market.getStatistics();
        for (IProduct product : statistics.getDischargedProducts()) {
            assertTrue("Discharged product price not less then minimal", product.getMinPrice() <= product.getPrice());
        }
    }

    private void validateCustomers(Market market) {
        MarketStatistics statistics = market.getStatistics();
        for (Customer customer : statistics.getCustomers()) {
            assertTrue("Customer balance not less then $0.00", customer.getBalance() >= 0.0);
            assertTrue("Customer balance not more then initial", customer.getBalance() <= customer.getInitialBalance());
            Double amountSpent = 0.0;
            for (IProduct product : customer.getProducts()) {
                amountSpent += product.getPrice();
                assertTrue("Product sale price not less then minimal", product.getMinPrice() <= product.getPrice());
            }
            assertEquals("Customer initial balance equals current balance plus amount spent", customer.getInitialBalance(),
                    (Double) (customer.getBalance() + amountSpent));
        }
    }
}
