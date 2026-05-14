public class CashPayment
        extends PaymentFramework {

    private double cashTendered;

    public CashPayment(
            String customerName,
            String transactionId,
            double originalAmount,
            double discountRate,
            double cashTendered) {

        super(
                customerName,
                transactionId,
                originalAmount,
                discountRate);

        this.cashTendered = cashTendered;
    }

    @Override
    public boolean validatePayment()
            throws Exception {

        return true;
    }

    @Override
    public double applyDiscount() {

        discountAmount = originalAmount *
                discountRate;

        return discountAmount;
    }

    @Override
    public void finalizeTransaction(
            double totalAmount) {

        System.out.println(
                "\nProceed to counter for payment.");

        System.out.printf(
                "TOTAL AMOUNT: PHP %,.2f%n",
                totalAmount);
    }

    @Override
    public String getPaymentMethod() {

        return "Cash";
    }

    public double getCashTendered() {

        return cashTendered;
    }

    public void setCashTendered(
            double cashTendered) {

        this.cashTendered = cashTendered;
    }
}