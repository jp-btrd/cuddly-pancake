package org.example;

public abstract class PaymentFramework {
    private String name;
    private String transactionID;
    private double amount;
    private boolean hasValidPaymentMethod;
    private double creditBalance;
    private double discountRate;

    protected double vatRate = 0.12;

    public PaymentFramework(String name, String transactionID, double amount,
                            boolean hasValidPaymentMethod, double creditBalance,
                            double discountRate) {
        this.name = name;
        this.transactionID = transactionID;
        this.amount = amount;
        this.hasValidPaymentMethod = hasValidPaymentMethod;
        this.creditBalance = creditBalance;
        this.discountRate = discountRate;
    }

    protected boolean validatePayment() {
        return hasValidPaymentMethod && creditBalance >= amount;
    }

    protected double applyVAT(double amount) {
        return amount * (1 + vatRate);
    }

    protected double applyDiscount(double amount) {
        return amount - (amount * discountRate);
    }

    protected void finalizeTransaction(double finalAmount) {
        creditBalance -= finalAmount;

        System.out.println("Transaction ID: " + transactionID);
        System.out.println("Customer Name: " + name);
        System.out.println("[!] Transaction Successful");
        System.out.println("Final Amount Paid: " + finalAmount);
        System.out.println("Remaining Balance: " + creditBalance);
    }

    public void processInvoice() {
        if (!validatePayment()) {
            System.out.println("Transaction ID: " + transactionID);
            System.out.println("[!] Payment Failed: Invalid method or insufficient balance.");
            return;
        }

        double total = applyVAT(amount);
        total = applyDiscount(total);

        finalizeTransaction(total);
    }
}