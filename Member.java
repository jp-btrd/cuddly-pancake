package org.example;

public class Member {
    private int memberId;
    private String name;
    private int age;
    private int pin;
    private double bal;

    public Member(int memberId, String name, int age, int pin, double bal) {
        this.memberId = memberId;
        this.name = name;
        this.age = age;
        this.pin = pin;
        this.bal = bal;
    }

    public double getBalance() { return bal; }
    public void setBalance(double bal) { this.bal = bal; }
    public void setPin(int newPin) { this.pin = newPin; }

    public int getMemberId() { return memberId; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public int getPin() { return pin; }
}
