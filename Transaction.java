public class Transaction {

    private String customerName;
    private String paymentMethod;
    private String transactionId;

    private double originalAmount;
    private double discountAmount;
    private double vatAmount;
    private double totalAmount;

    public Transaction(
            String customerName,
            String paymentMethod,
            String transactionId,
            double originalAmount,
            double discountAmount,
            double vatAmount,
            double totalAmount) {

        this.customerName = customerName;
        this.paymentMethod = paymentMethod;
        this.transactionId = transactionId;
        this.originalAmount = originalAmount;
        this.discountAmount = discountAmount;
        this.vatAmount = vatAmount;
        this.totalAmount = totalAmount;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public double getOriginalAmount() {
        return originalAmount;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public double getVatAmount() {
        return vatAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
}