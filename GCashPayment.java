public class GCashPayment extends PaymentFramework {

    private double walletBalance;
    private String mobileNumber;

    public GCashPayment(
            String customerName,
            String transactionId,
            double originalAmount,
            double discountRate,
            double walletBalance,
            String mobileNumber) {

        super(
                customerName,
                transactionId,
                originalAmount,
                discountRate);

        this.walletBalance = walletBalance;
        this.mobileNumber = mobileNumber;
    }

    @Override
    public boolean validatePayment() throws Exception {

        double estimate = applyTax(originalAmount * (1 - discountRate));

        if (walletBalance < estimate) {

            throw new Exception("Insufficient Wallet Balance.");
        }

        return true;
    }

    @Override
    public double applyDiscount() {

        return originalAmount * (1 - discountRate);
    }

    @Override
    public void finalizeTransaction(double totalAmount)
            throws Exception {

        walletBalance -= totalAmount;

        System.out.printf(
                "GCash Number : %s%n",
                mobileNumber);

        System.out.printf(
                "Charged Amount : PHP %,.2f%n",
                totalAmount);
    }

    @Override
    public String getPaymentMethod() {

        return "GCash";
    }
}