

import java.util.ArrayList;

public class StatService {
    private static UserDAO userDAO = new UserDAO();
    private static BookingDAO bookingDAO = new BookingDAO();

    public static ArrayList<User> getAllUsers() {
        return userDAO.getAll();
    }

    public static int getBookingCount() {
        return bookingDAO.getAll().size();
    }
}

