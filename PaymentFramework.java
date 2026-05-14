public abstract class PaymentFramework {

    protected static final double TAX_RATE = 0.12;

    protected String customerName;
    protected String transactionId;

    protected double originalAmount;
    protected double discountRate;

    protected double discountAmount;
    protected double vatAmount;
    protected double totalAmount;

    public PaymentFramework(
            String customerName,
            String transactionId,
            double originalAmount,
            double discountRate) {

        this.customerName = customerName;
        this.transactionId = transactionId;
        this.originalAmount = originalAmount;
        this.discountRate = discountRate;
    }

    public abstract boolean validatePayment() throws Exception;

    public abstract double applyDiscount();

    public abstract void finalizeTransaction(double totalAmount)
            throws Exception;

    public abstract String getPaymentMethod();

    public double applyTax(double amount) {

        return amount + (amount * TAX_RATE);
    }

    public void processInvoice() {

        try {

            printHeader();

            System.out.println("\n[1] VALIDATING PAYMENT");

            if (!validatePayment()) {

                throw new Exception("Payment validation failed.");
            }

            System.out.println("Payment Validated Successfully.");

            System.out.println("\n[2] APPLYING DISCOUNT");

            double discountedAmount = applyDiscount();

            discountAmount = originalAmount - discountedAmount;

            System.out.printf(
                    "Discount Amount : PHP %,.2f%n",
                    discountAmount);

            System.out.println("\n[3] APPLYING VAT");

            vatAmount = discountedAmount * TAX_RATE;

            totalAmount = discountedAmount + vatAmount;

            System.out.printf(
                    "VAT Amount      : PHP %,.2f%n",
                    vatAmount);

            System.out.printf(
                    "TOTAL AMOUNT    : PHP %,.2f%n",
                    totalAmount);

            System.out.println("\n[4] FINALIZING PAYMENT");

            finalizeTransaction(totalAmount);

            System.out.println("\nPAYMENT SUCCESSFUL!");

        }

        catch (Exception e) {

            System.out.println("\nPAYMENT FAILED : " + e.getMessage());
        }

        finally {

            printFooter();
        }
    }

    public Transaction generateTransactionRecord() {

        return new Transaction(
                customerName,
                getPaymentMethod(),
                transactionId,
                originalAmount,
                discountAmount,
                vatAmount,
                totalAmount);
    }

    private void printHeader() {

        System.out.println("\n======================================");
        System.out.println("        PAYMENT INVOICE SYSTEM");
        System.out.println("======================================");

        System.out.println("Transaction ID : " + transactionId);
        System.out.println("Customer Name  : " + customerName);

        System.out.printf(
                "Original Amount: PHP %,.2f%n",
                originalAmount);
    }

    private void printFooter() {

        System.out.println("======================================");
    }
}