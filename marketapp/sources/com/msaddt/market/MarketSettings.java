package com.msaddt.market;

public class MarketSettings {
    private long customerBuyPeriod;
    private long farmerProducePeriod;

    public long getCustomerBuyPeriod() {
        return customerBuyPeriod;
    }

    public void setCustomerBuyPeriod(long customerBuyPeriod) {
        this.customerBuyPeriod = customerBuyPeriod;
    }

    public long getFarmerProducePeriod() {
        return farmerProducePeriod;
    }

    public void setFarmerProducePeriod(long farmerProducePeriod) {
        this.farmerProducePeriod = farmerProducePeriod;
    }

    @Override
    public String toString() {
        return "MarketSettings [customerBuyPeriod=" + customerBuyPeriod + ", farmerProducePeriod=" + farmerProducePeriod + "]";
    }
}
