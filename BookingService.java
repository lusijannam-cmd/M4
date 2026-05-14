

import java.util.ArrayList;
import java.util.Scanner;

public class BookingService {

    private static BookingDAO bookingDAO = new BookingDAO();

    private static ExhibitDAO exhibitDAO = new ExhibitDAO();

    private static TransactionDAO transactionDAO = new TransactionDAO();

    public static void create(Scanner sc, User u) {

        ArrayList<String> ex = exhibitDAO.getAll();

        System.out.println("\n--- SELECT EXHIBIT ---");

        for (int i = 0; i < ex.size(); i++) {

            System.out.println(
                    "[" + (i + 1) + "] " + ex.get(i));
        }

        System.out.println("[0] Back");

        System.out.print("Choice: ");

        int choice = Main.getIntInput();

        if (choice > 0 && choice <= ex.size()) {

            System.out.print("Date (YYYY-MM-DD): ");
            String d = sc.next();

            System.out.print("Time: ");

            sc.nextLine();

            String t = sc.nextLine();

            

            if (isScheduleTaken(d, t)) {

                System.out.println(
                        "\nERROR: Schedule already reserved.");

                System.out.println(
                        "Please choose another schedule.");

                return;
            }

            System.out.print(
                    "Total Pax: ");

            int totalPax = Main.getIntInput();

            

            if (totalPax > 30) {

                System.out.println(
                        "\nERROR: Maximum of 30 pax only.");

                return;
            }

            if (totalPax <= 0) {

                System.out.println(
                        "\nERROR: Invalid pax count.");

                return;
            }

            int totalPeople = 0;

            System.out.println("\n================================");
            System.out.println("       CUSTOMER BREAKDOWN       ");
            System.out.println("================================");

            System.out.print("Regular Count: ");
            int regularCount = Main.getIntInput();
            totalPeople += regularCount;

            System.out.print("Student Count: ");
            int studentCount = Main.getIntInput();
            totalPeople += studentCount;

            System.out.print("Senior Count: ");
            int seniorCount = Main.getIntInput();
            totalPeople += seniorCount;

            System.out.print("PWD Count: ");
            int pwdCount = Main.getIntInput();
            totalPeople += pwdCount;

            System.out.print("VIP Count: ");
            int vipCount = Main.getIntInput();
            totalPeople += vipCount;

            if (totalPeople != totalPax) {

                System.out.println(
                        "\n[ ERROR ] Total customer count does not match total pax.");

                return;
            }

            double pricePerPax = 500;

            double regularTotal = regularCount * pricePerPax;

            double studentTotal = (studentCount * pricePerPax) * 0.90;

            double seniorTotal = (seniorCount * pricePerPax) * 0.80;

            double pwdTotal = (pwdCount * pricePerPax) * 0.80;

            double vipTotal = (vipCount * pricePerPax) * 0.85;

            double subtotal = totalPax * pricePerPax;

            double discountedTotal = regularTotal +
                    studentTotal +
                    seniorTotal +
                    pwdTotal +
                    vipTotal;

            double totalDiscount = subtotal - discountedTotal;

            double vat = discountedTotal * 0.12;

            double finalTotal = discountedTotal + vat;

            System.out.println("\n================================");
            System.out.println("        BOOKING SUMMARY         ");
            System.out.println("================================");

            System.out.printf(
                    "Exhibit              : %s%n",
                    ex.get(choice - 1));

            System.out.printf(
                    "Price Per Pax        : PHP %,.2f%n",
                    pricePerPax);

            System.out.printf(
                    "Total Pax            : %d%n",
                    totalPax);

            System.out.printf(
                    "Regular Guests       : %d%n",
                    regularCount);

            System.out.printf(
                    "Student Guests       : %d%n",
                    studentCount);

            System.out.printf(
                    "Senior Guests        : %d%n",
                    seniorCount);

            System.out.printf(
                    "PWD Guests           : %d%n",
                    pwdCount);

            System.out.printf(
                    "VIP Guests           : %d%n",
                    vipCount);

            System.out.printf(
                    "Subtotal             : PHP %,.2f%n",
                    subtotal);

            System.out.printf(
                    "Discount Amount      : PHP %,.2f%n",
                    totalDiscount);

            System.out.printf(
                    "VAT (12%%)            : PHP %,.2f%n",
                    vat);

            System.out.printf(
                    "FINAL TOTAL          : PHP %,.2f%n",
                    finalTotal);

            System.out.println("================================");

            System.out.println(
                    "\nNOTE: Valid ID must be presented for Student, Senior, and PWD discounts.");

            System.out.println("\n--------------------------------");
            System.out.println("        PAYMENT METHODS         ");
            System.out.println("--------------------------------");
            System.out.println("[1] Cash");
            System.out.println("[2] Credit Card");
            System.out.println("[3] GCash");

            System.out.print("Choose Payment Method: ");

            int paymentChoice = Main.getIntInput();

            PaymentFramework payment = null;

            String transactionId = "TXN-" + System.currentTimeMillis();

            double effectiveDiscountRate = totalDiscount / subtotal;

            if (paymentChoice == 1) {

                System.out.println(
                        "\nPlease proceed to the payment counter.");

                payment = new CashPayment(
                        u.getName(),
                        transactionId,
                        subtotal,
                        effectiveDiscountRate,
                        subtotal);
            }

            else if (paymentChoice == 2) {

                System.out.print("Card Number: ");

                String cardNumber = sc.next();

                System.out.print("Credit Limit: ");

                double limit = sc.nextDouble();

                payment = new CreditCardPayment(
                        u.getName(),
                        transactionId,
                        subtotal,
                        effectiveDiscountRate,
                        limit,
                        cardNumber);
            }

            else if (paymentChoice == 3) {

                System.out.print("Mobile Number: ");

                String mobile = sc.next();

                System.out.print("Wallet Balance: ");

                double balance = sc.nextDouble();

                payment = new GCashPayment(
                        u.getName(),
                        transactionId,
                        subtotal,
                        effectiveDiscountRate,
                        balance,
                        mobile);
            }

            if (payment != null) {

                try {

                    payment.validatePayment();

                    payment.processInvoice();

                   

                    if (paymentChoice == 1) {

                        bookingDAO.save(

                                new Booking.Builder()

                                        .setUserName(
                                                u.getName())

                                        .setExhibitName(
                                                ex.get(choice - 1))

                                        .setDate(d)

                                        .setTime(t)

                                        .setPax(totalPax)

                                        .setStatus(
                                                "Pending Payment")

                                        .build());

                        System.out.println(
                                "\n================================");

                        System.out.println(
                                " RESERVATION SUBMITTED SUCCESSFULLY ");

                        System.out.println(
                                " Please proceed to the counter.");

                        System.out.println(
                                " STATUS : Pending Payment");

                        System.out.println(
                                "================================");
                    }

                    

                    else {

                        transactionDAO.save(
                                payment.generateTransactionRecord());

                        bookingDAO.save(

                                new Booking.Builder()

                                        .setUserName(
                                                u.getName())

                                        .setExhibitName(
                                                ex.get(choice - 1))

                                        .setDate(d)

                                        .setTime(t)

                                        .setPax(totalPax)

                                        .setStatus("Paid")

                                        .build());

                        System.out.println(
                                "\nPAYMENT SUCCESSFUL!");
                    }
                }

                catch (Exception e) {

                    System.out.println(
                            "\n[ PAYMENT FAILED ] " + e.getMessage());

                    bookingDAO.save(
                            new Booking.Builder()
                                    .setUserName(u.getName())
                                    .setExhibitName(ex.get(choice - 1))
                                    .setDate(d)
                                    .setTime(t)
                                    .setPax(totalPax)
                                    .setStatus("Pending Payment")
                                    .build());

                    System.out.println(
                            "\nBooking saved as PENDING PAYMENT.");
                }
            }

            else {

                System.out.println(
                        "\n[ ERROR ] Invalid payment method.");
            }
        }
    }

    public static void viewMine(User u) {

        System.out.println("\n--- MY BOOKINGS ---");

        System.out.printf(
                "%-5s | %-15s | %-12s | %-20s\n",
                "ID",
                "Exhibit",
                "Date",
                "Status");

        for (Booking b : bookingDAO.getAll()) {

            if (b.getUserName().equals(u.getName())) {

                System.out.printf(
                        "%-5d | %-15s | %-12s | %-20s\n",
                        b.getId(),
                        b.getExhibitName(),
                        b.getDate(),
                        b.getStatus());
            }
        }
    }

    public static void adminManage() {

        System.out.println(
                "\n--- ALL PENDING/PROCESSED BOOKINGS ---");

        for (Booking b : bookingDAO.getAll()) {

            System.out.printf(
                    "ID:%d | User:%s | Exhibit:%s | Status:%s\n",
                    b.getId(),
                    b.getUserName(),
                    b.getExhibitName(),
                    b.getStatus());
        }

        System.out.print("ID to update (0 to back): ");

        int id = Main.getIntInput();

        if (id > 0) {

            System.out.print("[1] Approve [2] Reject: ");

            int act = Main.getIntInput();

            if (act == 1) {

                bookingDAO.updateStatus(
                        id,
                        "Approved");

                System.out.println(
                        "\nBOOKING APPROVED.");
            }

            else if (act == 2) {

                bookingDAO.updateStatus(
                        id,
                        "Refunded");

                System.out.println(
                        "\nBOOKING REJECTED.");

                System.out.println(
                        "CUSTOMER REFUND PROCESSED.");

                Transaction refund = new Transaction(
                        "SYSTEM",
                        "REFUND",
                        "REF-" + System.currentTimeMillis(),
                        0,
                        0,
                        0,
                        0);

                transactionDAO.save(refund);
            }
        }
    }

    public static void payPendingBooking(
            Scanner sc,
            User u) {

        ArrayList<Booking> pendingBookings = new ArrayList<>();

        System.out.println(
                "\n--- PENDING PAYMENTS ---");

        for (Booking b : bookingDAO.getAll()) {

            if (b.getUserName().equals(
                    u.getName())

                    &&

                    b.getStatus().equals(
                            "Pending Payment")) {

                pendingBookings.add(b);

                System.out.printf(
                        "ID:%d | Exhibit:%s | Date:%s | Pax:%d%n",

                        b.getId(),

                        b.getExhibitName(),

                        b.getDate(),

                        b.getPax());
            }
        }

        if (pendingBookings.isEmpty()) {

            System.out.println(
                    "No pending bookings.");

            return;
        }

        System.out.print(
                "\nEnter Booking ID to pay: ");

        int id = Main.getIntInput();

        Booking selected = null;

        for (Booking b : pendingBookings) {

            if (b.getId() == id) {

                selected = b;
                break;
            }
        }

        if (selected == null) {

            System.out.println(
                    "Invalid Booking ID.");

            return;
        }

        double subtotal = selected.getPax() * 500;

        double vat = subtotal * 0.12;

        double totalAmount = subtotal + vat;

        System.out.println(
                "\n========== PAYMENT SUMMARY ==========");

        System.out.printf(
                "Subtotal : PHP %,.2f%n",
                subtotal);

        System.out.printf(
                "VAT (12%%): PHP %,.2f%n",
                vat);

        System.out.printf(
                "TOTAL : PHP %,.2f%n",
                totalAmount);

        System.out.println(
                "=====================================");

        System.out.println("\n[1] Cash");
        System.out.println("[2] Credit Card");
        System.out.println("[3] GCash");

        System.out.print(
                "Choose payment method: ");

        int method = Main.getIntInput();

        PaymentFramework payment = null;

        String transactionId = "TXN-"
                + System.currentTimeMillis();

        

        if (method == 1) {

            System.out.println(
                    "\nPlease proceed to the counter.");

            bookingDAO.updateStatus(
                    id,
                    "Pending Payment");

            System.out.println(
                    "STATUS : Pending Payment");

            return;
        }

        

        else if (method == 2) {

            System.out.print(
                    "Card Number: ");

            String card = sc.next();

            System.out.print(
                    "Credit Limit: ");

            double limit = sc.nextDouble();

            payment = new CreditCardPayment(
                    u.getName(),
                    transactionId,
                    subtotal,
                    0,
                    limit,
                    card);
        }

        /*
         * GCASH
         */

        else if (method == 3) {

            System.out.print(
                    "Mobile Number: ");

            String mobile = sc.next();

            System.out.print(
                    "Wallet Balance: ");

            double balance = sc.nextDouble();

            payment = new GCashPayment(
                    u.getName(),
                    transactionId,
                    subtotal,
                    0,
                    balance,
                    mobile);
        }

        try {

            payment.validatePayment();

            payment.processInvoice();

            bookingDAO.updateStatus(
                    id,
                    "Paid");

            transactionDAO.save(
                    payment.generateTransactionRecord());

            System.out.println(
                    "\nPAYMENT SUCCESSFUL!");

        }

        catch (Exception e) {

            System.out.println(
                    "\nPAYMENT FAILED.");

            System.out.println(
                    "STATUS REMAINS : Pending Payment");
        }
    }

    public static boolean isScheduleTaken(
            String date,
            String time) {

        for (Booking b : bookingDAO.getAll()) {

            

            if (b.getStatus()
                    .equals("Approved")

                    &&

                    b.getDate()
                            .equalsIgnoreCase(date)

                    &&

                    b.getTime()
                            .equalsIgnoreCase(time)) {

                return true;
            }
        }

        return false;
    }
}

