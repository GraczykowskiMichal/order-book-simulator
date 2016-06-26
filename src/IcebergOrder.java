import java.util.Date;
import static java.lang.Math.min;

/**
 * Created by michalgraczykowski on 22.06.16.
 *
 * Represents Iceberg Order.
 */
public class IcebergOrder extends Order {
    private final int peak;
    private int visibleQuantity;

    /**
     * Constructor simply assigning values.
     * Assumes that the input data is correct.
     */
    public IcebergOrder(String direction, int id, int price, int quantity, int orderId, int peak) {
        super(direction, id, price, quantity, orderId);
        this.peak = peak;
        this.visibleQuantity = peak;
    }

    /**
     * Resets timeStamp of the Iceberg Order.
     */
    protected void resetTimeStamp() {
        /* timeStamp = current date */
        this.timeStamp = new Date();
    }

    /**
     * @return current visible quantity
     */
    @Override
    public int getQuantity() {
        return this.visibleQuantity;
    }


    /**
     * Decreases quantity of the Iceberg Order
     * and updates visiable quantity due to defined peak.
     * Assumes that amount <= this.visibleQuantity.
     *
     * @param amount amount to decrease
     */
    @Override
    protected void decreaseIncomingOrderQuantity(int amount) {
        /* Decrease quantity */
        this.visibleQuantity -= amount;
        this.quantity -= amount;

        /* Update visible quantity */
        if (this.quantity >= this.peak) {
            this.visibleQuantity = this.peak;
        } else {
            this.visibleQuantity = this.quantity;
        }
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

         /* if the visible quantity of the order == transactionAmount
           we need to reset the timeStamp. */
        if (this.getQuantity() == transactionAmount) {
            this.resetTimeStamp();
        }

        this.visibleQuantity -= transactionAmount;
        this.quantity -= transactionAmount;

        if (this.visibleQuantity == 0) {
            this.visibleQuantity = min(this.peak, this.quantity);
        }

        incomingOrder.decreaseIncomingOrderQuantity(transactionAmount);
        return transactionAmount;
    }
}
