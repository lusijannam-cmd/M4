

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static Scanner sc = new Scanner(System.in);
    static User loggedInUser = null;

    public static void main(String[] args) {
        showWelcomeBanner();
        while (true) {
            System.out.println("\n ==================================== ");
            System.out.println("            MAIN ACCESS PORTAL             ");
            System.out.println("==================================== ");
            System.out.println("  [1] REGISTER NEW ACCOUNT");
            System.out.println("  [2] LOGIN TO SYSTEM");
            System.out.println("  [3] SHUTDOWN SYSTEM");
            System.out.println("------------------------------------------------");
            System.out.print(" >> SELECT ACTION: ");

            int choice = getIntInput();
            if (choice == 1) {
                AccountService.register(sc);
            } else if (choice == 2) {
                loggedInUser = AccountService.login(sc);
                if (loggedInUser != null) {
                    System.out.println("\n[ SUCCESS ] Access Granted. Welcome, " + loggedInUser.getName() + "!");
                    if (loggedInUser.getRole().equalsIgnoreCase("Admin"))
                        adminMenu();
                    else
                        userMenu();
                } else {
                    System.out.println("\n[ ERROR ] Invalid Email or Password.");
                }
            } else if (choice == 3) {
                System.out.println("\nSystem shutting down... Goodbye!");
                break;
            } else {
                System.out.println("\n[ ! ] INVALID INPUT. Please try again.");
            }
        }
    }

    static void adminMenu() {

        while (true) {

            System.out.println("\n -------------------------------------------- ");
            System.out.println("             ADMINISTRATOR PANEL              ");
            System.out.println(" -------------------------------------------- ");
            System.out.println("  [1] MANAGE ALL BOOKINGS");
            System.out.println("  [2] MANAGE GALLERY EXHIBITS");
            System.out.println("  [3] VIEW SYSTEM STATISTICS");
            System.out.println("  [4] VIEW INCOME STATEMENT");
            System.out.println("  [5] LOGOUT");
            System.out.println(" -------------------------------------------- ");
            System.out.print(" >> SELECT OPTION: ");

            int c = getIntInput();

            if (c == 1)
                BookingService.adminManage();

            else if (c == 2)
                manageExhibits();

            else if (c == 3)
                displayFullStats();

            else if (c == 4)
                FinanceService.showIncomeStatement();

            else {

                System.out.println("\nLogging out...");
                loggedInUser = null;
                break;
            }
        }
    }

    static void userMenu() {
        while (true) {
            System.out.println("\n -------------------------------------------- ");
            System.out.println("               VISITOR DASHBOARD              ");
            System.out.println(" -------------------------------------------- ");
            System.out.println("  [1] BOOK AN EXHIBIT VISIT");
            System.out.println("  [2] VIEW MY BOOKING HISTORY");
            System.out.println("  [3] PAY PENDING BOOKING");
            System.out.println("  [4] LOGOUT");
            System.out.println(" -------------------------------------------- ");
            System.out.print(" >> SELECT OPTION: ");

            int c = getIntInput();
            if (c == 1)
                BookingService.create(sc, loggedInUser);

            else if (c == 2)
                BookingService.viewMine(loggedInUser);

            else if (c == 3)
                BookingService.payPendingBooking(sc, loggedInUser);

            else {
                System.out.println("\nLogging out...");
                loggedInUser = null;
                break;
            }
        }
    }

    static void displayFullStats() {
        ArrayList<User> userList = StatService.getAllUsers();
        int totalBookings = StatService.getBookingCount();

        System.out.println("\n ======================================================== ");
        System.out.println("                    SYSTEM STATISTICS LOG                       ");
        System.out.println(" ======================================================== ");
        System.out.println("  TOTAL REGISTERED USERS : " + userList.size());
        System.out.println("  TOTAL BOOKINGS MADE    : " + totalBookings);
        System.out.println("--------------------------------------------------------------------");

        System.out.println("\nLIST OF REGISTERED ACCOUNTS:");
        System.out.printf("  %-20s | %-25s | %-10s\n", "NAME", "EMAIL", "ROLE");
        System.out.println("  ------------------------------------------------------------------");

        if (userList.isEmpty()) {
            System.out.println("  [ ! ] No registered users found.");
        } else {
            for (User u : userList) {
                System.out.printf("  %-20s | %-25s | %-10s\n",
                        u.getName(),
                        u.getEmail(),
                        u.getRole());
            }
        }
        System.out.println("  ------------------------------------------------------------------");
        System.out.print("\n >> Press ENTER to return to Admin Panel...");
        sc.nextLine();
        sc.nextLine();
    }

    static void manageExhibits() {
        ExhibitDAO dao = new ExhibitDAO();
        while (true) {
            ArrayList<String> list = dao.getAll();
            System.out.println("\n--- CURRENT GALLERY EXHIBITS ---");
            if (list.isEmpty()) {
                System.out.println(" (Empty) ");
            } else {
                for (int i = 0; i < list.size(); i++) {
                    System.out.println(" " + (i + 1) + ". " + list.get(i));
                }
            }
            System.out.println("--------------------------------");
            System.out.println(" [1] Add  [2] Delete  [3] Back ");
            System.out.print(" >> ACTION: ");

            int act = getIntInput();
            if (act == 1) {
                System.out.print("Enter Exhibit Name: ");
                sc.nextLine();
                dao.add(sc.nextLine());
                System.out.println("\n[ OK ] Exhibit Added Successfully.");
            } else if (act == 2) {
                System.out.print("Enter Name to Delete: ");
                sc.nextLine();
                dao.delete(sc.nextLine());
                System.out.println("\n[ OK ] Exhibit Removed.");
            } else
                break;
        }
    }

    static void showWelcomeBanner() {
        System.out.println("________________________________________________");
        System.out.println("|                                              |");
        System.out.println("|          DLM ART MUSEUM AND GALLERY          |");
        System.out.println("|                                              |");
        System.out.println("|______________________________________________|");
    }

    public static int getIntInput() {
        try {
            int input = sc.nextInt();
            return input;
        } catch (Exception e) {
            sc.nextLine();
            return -1;
        }
    }
}

