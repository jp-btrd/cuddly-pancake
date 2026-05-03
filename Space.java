package org.example;

public class Space {
    private String spaceId;
    private double fixedRate;
    private boolean isOccupied;

    public Space(String spaceId, double fixedRate) {
        this.spaceId = spaceId;
        this.fixedRate = fixedRate;
        this.isOccupied = false;
    }

    public double calculateBill(int hrs) {
        return hrs * fixedRate;
    }

    public void setOccupied(boolean status) { this.isOccupied = status; }
    public void setFixedRate(double rate) { this.fixedRate = rate; }

    public String getSpaceId() { return spaceId; }
    public double getFixedRate() { return fixedRate; }
    public boolean isOccupied() { return isOccupied; }
}
