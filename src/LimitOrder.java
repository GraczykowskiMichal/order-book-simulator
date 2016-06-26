import static java.lang.Math.min;

/**
 * Created by michalgraczykowski on 22.06.16.
 *
 * Represents Limit Order.
 */
public class LimitOrder extends Order {
    /**
     * Constructor simply assigning values.
     * Assumes that the input data is correct.
     */
    public LimitOrder(String direction, int id, int price, int quantity, int orderId) {
        super(direction, id, price, quantity, orderId);
    }

    /**
     * Decreases quantity of the Limit Order.
     * Assumes that amount <= this.quantity.
     *
     * @param amount amount to decrease
     */
    @Override
    protected void decreaseIncomingOrderQuantity(int amount) {
        this.quantity -= amount;
    }

    /**
     * Runs transaction on the incoming Order.
     * Assumes that transaction can be made.
     *
     * @param incomingOrder incoming Order
     * @return amount of the transaction
     */
    @Override
    public int runTransactionOnIncomingOrder(Order incomingOrder) {
        int transactionAmount = min(this.getQuantity(), incomingOrder.getQuantity());

        this.quantity -= transactionAmount;

        incomingOrder.decreaseIncomingOrderQuantity(transactionAmount);
        return transactionAmount;
    }
}
