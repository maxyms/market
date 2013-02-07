package com.msaddt.market;

public class DefaultMarketSettings extends MarketSettings {
    @Override
    public long getCustomerBuyPeriod() {
        return 500;
    }

    @Override
    public long getFarmerProducePeriod() {
        return 1000;
    }

    @Override
    public long getLongTermMonitorFrequency() {
        return 100;
    }

    @Override
    public long getMaxTimeOnMarket() {
        return 3000;
    }
};
