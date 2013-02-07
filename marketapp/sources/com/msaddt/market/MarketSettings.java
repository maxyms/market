package com.msaddt.market;

public class MarketSettings {
    private long customerBuyPeriod;
    private long farmerProducePeriod;
    private long longTermMonitorFrequency;
    private long longTermTimeOnMarket;

    public long getLongTermTimeOnMarket() {
        return longTermTimeOnMarket;
    }

    public void setLongTermTimeOnMarket(long longTermTimeOnMarket) {
        this.longTermTimeOnMarket = longTermTimeOnMarket;
    }

    private long maxTimeOnMarket;

    public long getMaxTimeOnMarket() {
        return maxTimeOnMarket;
    }

    public void setMaxTimeOnMarket(long maxTimeOnMarket) {
        this.maxTimeOnMarket = maxTimeOnMarket;
    }

    public long getLongTermMonitorFrequency() {
        return longTermMonitorFrequency;
    }

    public void setLongTermMonitorFrequency(long longTermMonitorFrequency) {
        this.longTermMonitorFrequency = longTermMonitorFrequency;
    }

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
        return "MarketSettings [customerBuyPeriod=" + customerBuyPeriod + ", farmerProducePeriod=" + farmerProducePeriod
                + ", longTermMonitorFrequency=" + longTermMonitorFrequency + ", longTermTimeOnMarket=" + longTermTimeOnMarket
                + ", maxTimeOnMarket=" + maxTimeOnMarket + "]";
    }
}
