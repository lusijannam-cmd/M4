public class CreditCardPayment extends PaymentFramework {

    private double creditLimit;
    private String cardNumber;

    public CreditCardPayment(
            String customerName,
            String transactionId,
            double originalAmount,
            double discountRate,
            double creditLimit,
            String cardNumber) {

        super(
                customerName,
                transactionId,
                originalAmount,
                discountRate);

        this.creditLimit = creditLimit;
        this.cardNumber = cardNumber;
    }

    @Override
    public boolean validatePayment() throws Exception {

        double estimate = applyTax(originalAmount * (1 - discountRate));

        if (creditLimit < estimate) {

            throw new Exception("Insufficient Credit Limit.");
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

        creditLimit -= totalAmount;

        System.out.printf(
                "Card Number : %s%n",
                cardNumber);

        System.out.printf(
                "Charged Amount : PHP %,.2f%n",
                totalAmount);
    }

    @Override
    public String getPaymentMethod() {

        return "Credit Card";
    }
}