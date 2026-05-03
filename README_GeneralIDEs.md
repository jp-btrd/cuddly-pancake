# Co-Worker Space Hub — Setup & Run Guide

A step-by-step guide for setting up and running the project manually
by creating a Maven project and copying each file into it.

---

## Requirements

Make sure you have the following installed before starting:

| Tool | Version | Download |
|------|---------|----------|
| Java JDK | 21 | https://www.oracle.com/java/technologies/downloads/#java21 |
| IDE of your choice | Any | IntelliJ IDEA / Eclipse / NetBeans / VS Code |
| Maven | Built-in or standalone | https://maven.apache.org/download.cgi |

> **Note:** Most Java IDEs (IntelliJ IDEA, Eclipse, NetBeans) have Maven
> built in. If yours does, you do NOT need to install Maven separately.
> VS Code users will need the **Extension Pack for Java** from the marketplace.

---

## Step 1 — Create a New Maven Project

The steps differ slightly per IDE but the goal is the same —
create a new **Maven** project with the group ID `org.example`.

### IntelliJ IDEA
1. Open IntelliJ → Click **New Project**
2. Select **Maven Archetype** on the left
3. Set the following:
   - **Name:** `CoWorkerHub` (or any name you prefer)
   - **GroupId:** `org.example`
   - **ArtifactId:** `CoWorkerHub`
4. Click **Create**

### Eclipse
1. Go to **File → New → Maven Project**
2. Check **"Create a simple project"** and click Next
3. Set the following:
   - **Group Id:** `org.example`
   - **Artifact Id:** `CoWorkerHub`
4. Click **Finish**

### NetBeans
1. Go to **File → New Project**
2. Select **Maven → Java Application** and click Next
3. Set the following:
   - **Project Name:** `CoWorkerHub`
   - **Group Id:** `org.example`
4. Click **Finish**

### VS Code
1. Press `Ctrl+Shift+P` to open the Command Palette
2. Type and select **"Maven: Create Maven Project"**
3. Select **maven-archetype-quickstart**
4. Set **groupId** to `org.example` and **artifactId** to `CoWorkerHub`
5. Choose a folder to save the project and confirm

---

## Step 2 — Verify the Package Folder Structure

After creating the project, your IDE will generate a default folder structure.
Navigate to the following folder inside your project:

```
src/main/java/org/example/
```

This is where all the `.java` files will go. If the `org/example/` folders
were not created automatically, create them manually inside `src/main/java/`.

> The final structure should look like this:
> ```
> CoWorkerHub/
> ├── pom.xml
> └── src/
>     └── main/
>         └── java/
>             └── org/
>                 └── example/
>                     ├── Main.java
>                     ├── Member.java
>                     ├── PaymentFramework.java
>                     ├── RoomBookingPayment.java
>                     ├── Space.java
>                     └── WorkspaceManager.java
> ```

---

## Step 3 — Create Each Java Class and Paste the Code

Inside the `org/example/` folder, create the following **6 classes** one by one
and paste the corresponding code into each:

| Class to Create | Paste code from |
|-----------------|-----------------|
| `Main.java` | `Main.java` |
| `Member.java` | `Member.java` |
| `Space.java` | `Space.java` |
| `PaymentFramework.java` | `PaymentFramework.java` |
| `RoomBookingPayment.java` | `RoomBookingPayment.java` |
| `WorkspaceManager.java` | `WorkspaceManager.java` |

### How to create a class in each IDE

**IntelliJ IDEA**
Right-click the `org.example` package → New → Java Class → type the class name → Enter

**Eclipse**
Right-click the `org.example` package → New → Class → type the class name → Finish

**NetBeans**
Right-click the `org.example` package → New → Java Class → type the class name → Finish

**VS Code**
Right-click the `org/example/` folder → New File → type the full filename (e.g. `Main.java`)

> Make sure every file starts with `package org.example;` at the top.
> This should already be there when you paste the code — just double check.

---

## Step 4 — Set Up the pom.xml

When Maven creates a new project, it generates a `pom.xml` file automatically
in the root of your project. You need to **replace its entire contents** with
the provided `pom.xml` code.

1. Open `pom.xml` from the root of your project
2. Select all the existing content (`Ctrl+A`)
3. Delete it and paste the provided `pom.xml` content in its place
4. Save the file

The `pom.xml` tells Maven to download the **SQLite JDBC driver** automatically,
which is required for the database to work. Without this step, the program
will fail to connect to the database.

---

## Step 5 — Load the Maven Dependencies

After saving `pom.xml`, you need to tell Maven to download the SQLite driver.

**IntelliJ IDEA**
A popup will appear saying "Maven projects need to be imported" — click **Load**.
If it doesn't appear, open the Maven panel on the right and click the
**Reload All Maven Projects** button (circular arrow icon).

**Eclipse**
Right-click the project → **Maven → Update Project** → click OK.

**NetBeans**
Right-click the project → **Clean and Build**. NetBeans will resolve
dependencies automatically.

**VS Code**
Open the terminal (`Ctrl+`` `) and run:
```
mvn install
```

> Once done, the SQLite JDBC driver will be downloaded automatically.
> You do not need to find or add any `.jar` file manually.

---

## Step 6 — Set the Java Version to 21

Make sure your project is using Java 21, which is already specified in the `pom.xml`.

**IntelliJ IDEA**
Go to **File → Project Structure → Project** → set SDK to Java 21.

**Eclipse**
Right-click the project → **Properties → Java Compiler** → set to 21.

**NetBeans**
Right-click the project → **Properties → Sources** → set Source/Binary Format to 21.

**VS Code**
Make sure JDK 21 is installed. VS Code will detect it automatically
if it's set as your default Java version.

---

## Step 7 — Run the Project

Run the `Main.java` file to start the program.

**IntelliJ IDEA**
Open `Main.java` → click the green ▶ arrow next to `public static void main` →
select **Run 'Main.main()'**

**Eclipse**
Open `Main.java` → Right-click anywhere in the file →
**Run As → Java Application**

**NetBeans**
Open `Main.java` → click the green ▶ Run button in the toolbar,
or right-click the file → **Run File**

**VS Code**
Open `Main.java` → click **Run** above the `main` method,
or press `F5`

The terminal should display:
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

---

## Step 8 — First Time Usage Flow

Follow this order when using the program for the first time:

**1. Register an account**
Select `[2] Register` → Enter your Full Name, a numeric Member ID
(e.g. `1001`), Age (must be 18 or older), and a 4-digit PIN.

**2. Login**
Select `[1] Login` → Enter the Member ID and PIN you just registered.

**3. Top Up your balance**
Select `4. Top Up Balance` from the dashboard → Enter an amount.
Keep in mind that booking includes 12% VAT on top of the base rate:

| Duration | Base Cost | VAT (12%) | Total |
|----------|-----------|-----------|-------|
| 1 hour   | ₱500.00   | ₱60.00    | ₱560.00 |
| 2 hours  | ₱1,000.00 | ₱120.00   | ₱1,120.00 |
| 3 hours  | ₱1,500.00 | ₱180.00   | ₱1,680.00 |

**4. View available rooms**
Select `1. View Available Rooms`. Rooms tagged with `[OC]` are already taken.

Available Room IDs:
| Floor 3 | Floor 2 | Floor 1 |
|---------|---------|---------|
| 3R1     | 2R1     | 1R1     |
| 3R2     | 2R2     | 1R2     |
| 3R3     | 2R3     | 1R3     |

**5. Book a room**
Select `2. Book a Room` → Enter a Room ID and duration in hours → Confirm with `Y`.
The system will print a receipt showing Transaction ID, amount paid (VAT inclusive),
and your remaining balance.

**6. Cancel a booking**
Select `3. Cancel a Booking` → Enter the Room ID you booked → Confirm with `Y`.
Your balance will be refunded automatically.

---

## Important Notes

**Database file**
A file called `coworker_hub.db` will be created automatically in your
project root the first time you run the program. This stores all members,
rooms, and bookings. Do not delete it while the program is running.
If you want to start completely fresh, close the program first,
delete `coworker_hub.db`, then run again.

**Each groupmate runs their own local database**
Since the database is a local file, every person who sets up the project
will have their own separate `coworker_hub.db`. Data is not shared between machines.

---

## Troubleshooting

**"No suitable driver found for jdbc:sqlite"**
Maven dependencies were not loaded. Go back to Step 5 and reload/update Maven.

**"Package org.example does not exist" or red underlines everywhere**
The class files are not in the correct package folder.
Make sure all `.java` files are inside `src/main/java/org/example/`
and that each file starts with `package org.example;`.

**"Cannot find or load main class"**
Make sure you are running `Main.java` specifically, not any other file.

**"Invalid age input" on registration**
Age must be 18 or older and must be a whole number with no decimals.

**"Insufficient balance" on booking**
Top up your balance first (option 4). Refer to the VAT cost table above.

**Program runs but shows DB errors on startup**
Delete `coworker_hub.db` from your project root and run the program again.

---

## Class Overview

| Class | Role |
|-------|------|
| `Main.java` | Handles all user input and menu display |
| `WorkspaceManager.java` | Core logic — members, rooms, bookings, DB |
| `Member.java` | Data model for a registered member |
| `Space.java` | Data model for a co-working room |
| `PaymentFramework.java` | Abstract class — standardizes payment flow (VAT, discount, validation) |
| `RoomBookingPayment.java` | Extends PaymentFramework — used for room booking transactions |
| `pom.xml` | Maven project config — declares the SQLite JDBC dependency so it downloads automatically |

---
