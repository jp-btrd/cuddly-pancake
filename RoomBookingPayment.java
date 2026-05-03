package org.example;

public class RoomBookingPayment extends PaymentFramework {

    public RoomBookingPayment(String name, String transactionID, double amount,
                              boolean hasValidPaymentMethod, double creditBalance,
                              double discountRate) {
        super(name, transactionID, amount, hasValidPaymentMethod, creditBalance, discountRate);
    }
}