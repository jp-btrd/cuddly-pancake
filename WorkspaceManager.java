package org.example;

import java.sql.*;
import java.util.*;

public class WorkspaceManager {
    private List<Member> members = new ArrayList<>();
    private List<Space> spaces = new ArrayList<>();
    private Member currentUser;

    private Connection conn;
    private static final String DB_URL = "jdbc:sqlite:coworker_hub.db";

    public WorkspaceManager() {
        initDatabase();
        loadSpacesFromDB();
        loadMembersFromDB();
    }

    private void initDatabase() {
        try {
            conn = DriverManager.getConnection(DB_URL);
            createTables();
        } catch (SQLException e) {
            System.out.println("[DB ERROR] Could not connect to database: " + e.getMessage());
        }
    }

    private void createTables() throws SQLException {
        Statement stmt = conn.createStatement();

        stmt.execute(
                "CREATE TABLE IF NOT EXISTS members (" +
                        "  member_id   INTEGER PRIMARY KEY," +
                        "  name        TEXT    NOT NULL," +
                        "  age         INTEGER NOT NULL," +
                        "  pin         INTEGER NOT NULL," +
                        "  balance     REAL    DEFAULT 0.0" +
                        ")"
        );

        stmt.execute(
                "CREATE TABLE IF NOT EXISTS spaces (" +
                        "  space_id    TEXT    PRIMARY KEY," +
                        "  fixed_rate  REAL    NOT NULL," +
                        "  is_occupied INTEGER DEFAULT 0" +
                        ")"
        );

        stmt.execute(
                "CREATE TABLE IF NOT EXISTS bookings (" +
                        "  booking_id  INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "  member_id   INTEGER NOT NULL," +
                        "  space_id    TEXT    NOT NULL," +
                        "  duration    INTEGER NOT NULL," +
                        "  total_cost  REAL    NOT NULL," +
                        "  FOREIGN KEY (member_id) REFERENCES members(member_id)," +
                        "  FOREIGN KEY (space_id)  REFERENCES spaces(space_id)" +
                        ")"
        );

        ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM spaces");
        if (rs.next() && rs.getInt(1) == 0) {
            String[] ids = {"3R1","3R2","3R3","2R1","2R2","2R3","1R1","1R2","1R3"};
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO spaces (space_id, fixed_rate, is_occupied) VALUES (?, 500.00, 0)"
            );
            for (String id : ids) {
                ps.setString(1, id);
                ps.executeUpdate();
            }
            ps.close();
        }

        stmt.close();
    }

    private void loadSpacesFromDB() {
        spaces.clear();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT space_id, fixed_rate, is_occupied FROM spaces");
            while (rs.next()) {
                Space s = new Space(rs.getString("space_id"), rs.getDouble("fixed_rate"));
                s.setOccupied(rs.getInt("is_occupied") == 1);
                spaces.add(s);
            }
            stmt.close();
        } catch (SQLException e) {
            System.out.println("[DB ERROR] Failed to load spaces: " + e.getMessage());
        }
    }

    private void loadMembersFromDB() {
        members.clear();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT member_id, name, age, pin, balance FROM members");
            while (rs.next()) {
                members.add(new Member(
                        rs.getInt("member_id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getInt("pin"),
                        rs.getDouble("balance")
                ));
            }
            stmt.close();
        } catch (SQLException e) {
            System.out.println("[DB ERROR] Failed to load members: " + e.getMessage());
        }
    }

    public void registerMember(String name, int age, String user, String pass) {
        if (age < 18) {
            System.out.println("[!] Invalid age input. Must be older than 18.");
            return;
        }
        int memberId;
        try {
            memberId = Integer.parseInt(user.trim());
        } catch (NumberFormatException e) {
            System.out.println("[!] Invalid Member ID. Must be a number.");
            return;
        }

        for (Member m : members) {
            if (m.getMemberId() == memberId) {
                System.out.println("[!] Member ID already exists.");
                return;
            }
        }

        try {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO members (member_id, name, age, pin, balance) VALUES (?, ?, ?, ?, 0.0)"
            );
            ps.setInt(1, memberId);
            ps.setString(2, name);
            ps.setInt(3, age);
            ps.setInt(4, Integer.parseInt(pass));
            ps.executeUpdate();
            ps.close();

            members.add(new Member(memberId, name, age, Integer.parseInt(pass), 0.0));
            System.out.println("[!] ACCOUNT CREATED SUCCESSFULLY!");

        } catch (SQLException e) {
            System.out.println("[DB ERROR] Registration failed: " + e.getMessage());
        }
    }

    public boolean login(String user, String pass) {
        for (Member m : members) {
            if (String.valueOf(m.getMemberId()).equals(user) &&
                    String.valueOf(m.getPin()).equals(pass)) {
                currentUser = m;
                return true;
            }
        }
        return false;
    }

    public void displayAvailableSpaces() {
        loadSpacesFromDB();
        System.out.println("------------------------------------------------------------------");
        System.out.println("CO-WORKER SPACE HUB [VIEW ROOMS]");
        System.out.println("RATE: \u20b1500.00/hour (VAT inclusive)\t[OC] - OCCUPIED");
        System.out.println("------------------------------------------------------------------");
        int count = 0;
        for (Space s : spaces) {
            String status = s.isOccupied() ? " - OC" : "";
            System.out.print("[" + s.getSpaceId() + status + "]\t\t");
            if (++count % 3 == 0) System.out.println();
        }
        System.out.println();
    }

    public void finalizeBooking(String spaceId, int duration) {
        loadSpacesFromDB();
        for (Space s : spaces) {
            if (s.getSpaceId().equalsIgnoreCase(spaceId)) {
                if (s.isOccupied()) {
                    System.out.println("[!] TRANSACTION UNSUCCESSFUL");
                    System.out.println("[!] This room is already occupied!");
                } else {
                    double baseAmount   = s.calculateBill(duration);   // e.g. 500 x hrs
                    double discountRate = 0.0;                          // no discount
                    String transactionID = "TXN-" + spaceId + "-" + currentUser.getMemberId();

                    // ── DELEGATE TO PAYMENT FRAMEWORK ─────────────────────────
                    // RoomBookingPayment extends PaymentFramework.
                    // processInvoice() will:
                    //   1. validatePayment()      — checks hasValidPaymentMethod && balance >= amount
                    //   2. applyVAT(amount)       — amount * 1.12
                    //   3. applyDiscount(total)   — total - (total * discountRate)
                    //   4. finalizeTransaction()  — prints receipt, deducts from its creditBalance copy
                    RoomBookingPayment payment = new RoomBookingPayment(
                            currentUser.getName(),
                            transactionID,
                            baseAmount,
                            true,                       // hasValidPaymentMethod
                            currentUser.getBalance(),   // creditBalance
                            discountRate
                    );

                    // Compute the same final amount the framework will use
                    double vatTotal    = baseAmount * (1 + 0.12);
                    double finalAmount = vatTotal - (vatTotal * discountRate);

                    // Check balance before calling framework so we can exit cleanly
                    if (currentUser.getBalance() < finalAmount) {
                        // Let the framework print the failure message
                        payment.processInvoice();
                        return;
                    }

                    // Balance is sufficient — framework prints the receipt
                    payment.processInvoice();

                    // ── SYNC BACK TO DB ───────────────────────────────────────
                    try {
                        // Record booking with VAT-inclusive cost
                        PreparedStatement ps = conn.prepareStatement(
                                "INSERT INTO bookings (member_id, space_id, duration, total_cost) VALUES (?, ?, ?, ?)"
                        );
                        ps.setInt(1, currentUser.getMemberId());
                        ps.setString(2, s.getSpaceId());
                        ps.setInt(3, duration);
                        ps.setDouble(4, finalAmount);
                        ps.executeUpdate();
                        ps.close();

                        // Mark space occupied
                        PreparedStatement ps2 = conn.prepareStatement(
                                "UPDATE spaces SET is_occupied = 1 WHERE space_id = ?"
                        );
                        ps2.setString(1, s.getSpaceId());
                        ps2.executeUpdate();
                        ps2.close();

                        // Deduct from Member object and persist to DB
                        double newBalance = currentUser.getBalance() - finalAmount;
                        currentUser.setBalance(newBalance);

                        PreparedStatement ps3 = conn.prepareStatement(
                                "UPDATE members SET balance = ? WHERE member_id = ?"
                        );
                        ps3.setDouble(1, newBalance);
                        ps3.setInt(2, currentUser.getMemberId());
                        ps3.executeUpdate();
                        ps3.close();

                        s.setOccupied(true);

                    } catch (SQLException e) {
                        System.out.println("[DB ERROR] Booking failed: " + e.getMessage());
                    }
                }
                return;
            }
        }
        System.out.println("[!] Room ID not found.");
    }

    public void cancelBooking(String spaceId) {
        loadSpacesFromDB();
        for (Space s : spaces) {
            if (s.getSpaceId().equalsIgnoreCase(spaceId)) {

                if (!s.isOccupied()) {
                    System.out.println("[!] This room is not currently booked.");
                    return;
                }

                // ── OWNERSHIP CHECK ───────────────────────────────────────────
                try {
                    PreparedStatement checkPs = conn.prepareStatement(
                            "SELECT total_cost FROM bookings WHERE space_id = ? AND member_id = ?"
                    );
                    checkPs.setString(1, s.getSpaceId());
                    checkPs.setInt(2, currentUser.getMemberId());
                    ResultSet rs = checkPs.executeQuery();

                    if (!rs.next()) {
                        System.out.println("[!] CANCELLATION FAILED.");
                        System.out.println("[!] You do not have an active booking for Room " + spaceId + ".");
                        checkPs.close();
                        return;
                    }

                    double refundAmount = rs.getDouble("total_cost");
                    checkPs.close();

                    // Remove booking record
                    PreparedStatement deletePs = conn.prepareStatement(
                            "DELETE FROM bookings WHERE space_id = ? AND member_id = ?"
                    );
                    deletePs.setString(1, s.getSpaceId());
                    deletePs.setInt(2, currentUser.getMemberId());
                    int rowsDeleted = deletePs.executeUpdate();
                    deletePs.close();

                    if (rowsDeleted == 0) {
                        System.out.println("[!] CANCELLATION FAILED. No matching booking record found.");
                        return;
                    }

                    // Free the space
                    PreparedStatement updatePs = conn.prepareStatement(
                            "UPDATE spaces SET is_occupied = 0 WHERE space_id = ?"
                    );
                    updatePs.setString(1, s.getSpaceId());
                    updatePs.executeUpdate();
                    updatePs.close();

                    // ── REFUND BALANCE ────────────────────────────────────────
                    double newBalance = currentUser.getBalance() + refundAmount;
                    currentUser.setBalance(newBalance);

                    PreparedStatement refundPs = conn.prepareStatement(
                            "UPDATE members SET balance = ? WHERE member_id = ?"
                    );
                    refundPs.setDouble(1, newBalance);
                    refundPs.setInt(2, currentUser.getMemberId());
                    refundPs.executeUpdate();
                    refundPs.close();

                    s.setOccupied(false);
                    System.out.println("[!] BOOKING CANCELLED SUCCESSFULLY.");
                    System.out.println("[!] Room " + spaceId + " is now available.");
                    System.out.printf("[!] Refund Amount : \u20b1%.2f%n", refundAmount);
                    System.out.printf("[!] New Balance   : \u20b1%.2f%n", newBalance);

                } catch (SQLException e) {
                    System.out.println("[DB ERROR] Cancellation failed: " + e.getMessage());
                }
                return;
            }
        }
        System.out.println("[!] Room ID not found.");
    }

    public void topUpBalance(double amount) {
        if (amount <= 0) {
            System.out.println("[!] Invalid amount. Please enter a positive value.");
            return;
        }

        try {
            double newBalance = currentUser.getBalance() + amount;
            currentUser.setBalance(newBalance);

            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE members SET balance = ? WHERE member_id = ?"
            );
            ps.setDouble(1, newBalance);
            ps.setInt(2, currentUser.getMemberId());
            ps.executeUpdate();
            ps.close();

            System.out.println("[!] TOP UP SUCCESSFUL!");
            System.out.printf("[!] Amount Added  : \u20b1%.2f%n", amount);
            System.out.printf("[!] New Balance   : \u20b1%.2f%n", newBalance);

        } catch (SQLException e) {
            System.out.println("[DB ERROR] Top up failed: " + e.getMessage());
        }
    }

    public Member getCurrentUser() { return currentUser; }

    public void logout() {
        currentUser = null;
    }

    public void closeDatabase() {
        try {
            if (conn != null && !conn.isClosed()) conn.close();
        } catch (SQLException e) {
            System.out.println("[DB ERROR] Failed to close database: " + e.getMessage());
        }
    }
}