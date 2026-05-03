# Co-Worker Space Hub — Setup & Run Guide

## Step 1 — Clone the Repository

1. Open **IntelliJ IDEA**
2. On the Welcome screen, click **"Get from VCS"**
   - If you already have a project open, go to **File → New → Project from Version Control**
3. Paste the GitHub repository URL into the URL field
4. Choose a folder on your computer where you want to save the project
5. Click **Clone**
6. Wait for IntelliJ to finish cloning — it will open the project automatically

---

## Step 2 — Verify the Project Structure

Once the project is open, make sure your file structure looks exactly like this
inside the **Project** panel on the left side:

```
CoWorkerHub/
├── pom.xml                        ← Maven config (already included)
└── src/
    └── main/
        └── java/
            └── org/
                └── example/
                    ├── Main.java
                    ├── Member.java
                    ├── PaymentFramework.java
                    ├── RoomBookingPayment.java
                    ├── Space.java
                    └── WorkspaceManager.java
```

> If any of the `.java` files are missing, ask your groupmate to push them to GitHub first.

---

## Step 3 — Load Maven Dependencies

This step downloads the **SQLite JDBC driver** automatically using your `pom.xml`.
You only need to do this once.

1. Look at the top-right corner of IntelliJ — you should see a small **Maven** icon (an "M")
2. Click it to open the **Maven panel**
3. Click the **Reload All Maven Projects** button (the circular arrow icon)
4. Wait for the download to finish — you'll see a progress bar at the bottom

**Alternative if you don't see the Maven panel:**
- Right-click on `pom.xml` in the project panel
- Click **"Add as Maven Project"**
- Then repeat steps 3–4 above

> Once this is done, IntelliJ will have downloaded `sqlite-jdbc-3.45.1.0.jar`
> automatically. You don't need to find or add it manually.

---

## Step 4 — Verify Java SDK is Set to 21

1. Go to **File → Project Structure** (or press `Ctrl+Alt+Shift+S`)
2. Under **Project**, check that **SDK** is set to **Java 21**
3. Check that **Language Level** is also set to **21**
4. Click **OK**

> If Java 21 is not listed, click **"Add SDK" → "Download JDK"** and select version 21.

---

## Step 5 — Run the Project

1. In the project panel, navigate to:
   `src → main → java → org → example → Main.java`
2. Open `Main.java`
3. Look for the green **Run** arrow (▶) next to `public static void main(String[] args)`
4. Click it and select **"Run 'Main.main()'"**
5. The terminal at the bottom of IntelliJ will open and show:

```
------------------------------------------------------------------
WELCOME TO CO-WORKER SPACE HUB
------------------------------------------------------------------
[1] Login (Existing Member)
[2] Register (New Member)
[3] Exit
------------------------------------------------------------------
Selection >
```

The program is now running. Use the terminal at the bottom to interact with it.

---

## Step 6 — First Time Usage Flow

Since the database starts empty, follow this order when testing for the first time:

### 1. Register an account
- Select `[2] Register`
- Enter your Full Name, a numeric Member ID (e.g. `1001`), Age (must be 18+), and a 4-digit PIN

### 2. Login
- Select `[1] Login`
- Enter the Member ID and PIN you just created

### 3. Top Up your balance
- From the dashboard, select `4. Top Up Balance`
- Enter an amount — remember that booking costs ₱500/hour **plus 12% VAT**
- So for 1 hour: ₱500 × 1.12 = **₱560.00** minimum

### 4. View available rooms
- Select `1. View Available Rooms`
- Available rooms are shown without any tag
- Occupied rooms are shown with `[OC]`

### 5. Book a room
- Select `2. Book a Room`
- Enter a Room ID (e.g. `3R1`) and duration in hours
- Confirm with `Y`
- The framework will print the full receipt including Transaction ID, VAT-inclusive amount, and remaining balance

### 6. Cancel a booking
- Select `3. Cancel a Booking`
- Enter the Room ID you booked
- Confirm with `Y`
- Your balance will be refunded automatically

---

## Important Notes

### Database file
- A file called `coworker_hub.db` will be created automatically in your
  **project root folder** the first time you run the program
- This file stores all members, rooms, and bookings
- **Do not delete it while the program is running**
- If you want to start completely fresh (wipe all data), close the program first,
  then delete `coworker_hub.db`, and run again

### Booking costs (VAT inclusive)
| Duration | Base Cost | VAT (12%) | Total |
|----------|-----------|-----------|-------|
| 1 hour   | ₱500.00   | ₱60.00    | ₱560.00 |
| 2 hours  | ₱1,000.00 | ₱120.00   | ₱1,120.00 |
| 3 hours  | ₱1,500.00 | ₱180.00   | ₱1,680.00 |

### Room IDs
The system has 9 rooms across 3 floors:

| Floor 3 | Floor 2 | Floor 1 |
|---------|---------|---------|
| 3R1     | 2R1     | 1R1     |
| 3R2     | 2R2     | 1R2     |
| 3R3     | 2R3     | 1R3     |

---

## Troubleshooting

**"No suitable driver found for jdbc:sqlite"**
- Maven dependencies were not loaded properly
- Go back to Step 3 and reload Maven projects

**"Cannot find main class"**
- Make sure you are running `Main.java` specifically, not any other file
- Right-click `Main.java` → Run 'Main.main()'

**"Invalid age input" on registration**
- Age must be 18 or older and must be a whole number (no decimals)

**"Insufficient balance" on booking**
- Top up your balance first (option 4 in the dashboard)
- Remember the VAT-inclusive cost from the table above

**Program compiles but shows DB errors on startup**
- Delete `coworker_hub.db` from the project root and run again

---

## Class Overview (For Reference)

| Class | Role |
|-------|------|
| `Main.java` | Handles all user input and menu display |
| `WorkspaceManager.java` | Core logic — members, rooms, bookings, DB |
| `Member.java` | Data model for a registered member |
| `Space.java` | Data model for a co-working room |
| `PaymentFramework.java` | Abstract class — standardizes payment flow (VAT, discount, validation) |
| `RoomBookingPayment.java` | Extends PaymentFramework — used for room booking transactions |
