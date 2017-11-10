package com.sweng.customerqueue.web;

public enum Reason {
    ACCTISSUES("Account Issues","acctissues"),
    BILLINGPAYMENT("Billing/Payment","billingpayment"),
    DEVICEUPGRADE("Device Upgrade","deviceupgrade"),
    NEWCUSTOMER("New Customer","newcustomer"),
    TECHSUPPORT("Technical Support","techsupport"),
    OTHER("Other","other");

    private final String name;
    private final String reason;

    Reason(String name, String reason) {
        this.name = name;
        this.reason = reason;
    }

    public String getName() {
        return name;
    }

    public String getReason() {
        return reason;
    }
}