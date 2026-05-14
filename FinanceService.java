import java.util.ArrayList;

public class FinanceService {

        private static BookingDAO bookingDAO = new BookingDAO();

        public static void showIncomeStatement() {

                ArrayList<Booking> bookings = bookingDAO.getAll();

                double grossIncome = 0;

                double vatTotal = 0;

                double netIncome = 0;

                int approvedCount = 0;

                System.out.println(
                                "\n===== INCOME STATEMENT =====");

                for (Booking b : bookings) {

                        if (b.getStatus()
                                        .equals("Approved")) {

                                approvedCount++;

                                double subtotal = b.getPax() * 500;

                                double vat = subtotal * 0.12;

                                double total = subtotal + vat;

                                grossIncome += subtotal;

                                vatTotal += vat;

                                netIncome += total;
                        }
                }

                System.out.printf(
                                "Approved Bookings : %d%n",
                                approvedCount);

                System.out.printf(
                                "Gross Income      : PHP %,.2f%n",
                                grossIncome);

                System.out.printf(
                                "VAT Collected     : PHP %,.2f%n",
                                vatTotal);

                System.out.printf(
                                "Net Income        : PHP %,.2f%n",
                                netIncome);

                System.out.println(
                                "==============================");
        }
}