

import java.util.*;

public class AccountService {
    private static UserDAO userDAO = new UserDAO();

    public static void register(Scanner sc) {
        System.out.println("\n--- REGISTRATION ---");
        System.out.print("Full Name: ");
        sc.nextLine();
        String n = sc.nextLine();
        System.out.print("Email: ");
        String e = sc.next();
        System.out.print("Password: ");
        String p = sc.next();
        userDAO.save(new User.Builder().setName(n).setEmail(e).setPassword(p).setRole("User").build());
        System.out.println("User Registered!");
    }

    public static User login(Scanner sc) {
        System.out.println("\n--- LOGIN ---");
        System.out.print("Email: ");
        String e = sc.next();
        System.out.print("Password: ");
        String p = sc.next();
        for (User u : userDAO.getAll()) {
            if (u.getEmail().equalsIgnoreCase(e) && u.getPassword().equals(p))
                return u;
        }
        return null;
    }
}


