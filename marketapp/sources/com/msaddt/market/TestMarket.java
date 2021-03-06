package com.msaddt.market;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.msaddt.market.manufacturer.Farmer;

public class TestMarket {
    private static final Logger logger = LoggerFactory.getLogger(TestMarket.class);

    public static void main(String... strings) throws InterruptedException {
        logger.debug("-----------Start Market Aplication-----------");
        final MarketSettings settings = new MarketSettings();
        settings.setCustomerBuyPeriod(500);
        settings.setFarmerProducePeriod(1000);
        settings.setLongTermMonitorFrequency(100);
        settings.setMaxTimeOnMarket(2000);
        settings.setLongTermTimeOnMarket(1000);
        Market market = new Market(settings);
        int farmers = 3;
        for (int i = 0; i < farmers; i++) {
            Farmer farmer = new Farmer("Farmer " + (i + 1));
            market.addManufacturer(farmer);
        }
        int customers = 10;
        for (int i = 0; i < customers; i++) {
            Double balance = Math.ceil(Math.random() * 100);
            Customer customer = new Customer("Customer " + (i + 1), balance);
            market.addCustomer(customer);
        }
        market.open();
        Thread.sleep(10000);
        market.close();
        logger.debug(market.toString());
        logger.debug("-----------End Market Aplication-----------");
    }
}
