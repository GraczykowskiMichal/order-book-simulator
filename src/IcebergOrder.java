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
    protected void decreaseQuantity(int amount) {
        /* Decrease quantity */
        this.visibleQuantity -= amount;
        this.quantity -= amount;

        /* If end of the peak is reached
           update visible quantity */
        if (this.visibleQuantity == 0) {
            this.visibleQuantity = min(this.quantity, this.peak);
        }
    }

    /**
     * Runs transaction on the Iceberg Order
     * assuming that this method is called
     * on the Iceberg Order that just joined the Order Book.
     * Assumes that transaction can be made.
     *
     * @param other Order to make the transaction with
     * @return amount of the transaction
     */
    @Override
    public int runTransaction(Order other) {
        int transactionAmount = min(this.getQuantity(), other.getQuantity());
        this.decreaseQuantity(transactionAmount);

        /* if the visible quantity of the order == transactionAmount
           we need to reset the timeStamp. If other is Limit Order it will
           be removed from the Order Book. */
        if (other.getQuantity() == transactionAmount) {
            other.resetTimeStamp();
        }
        other.decreaseQuantity(transactionAmount);
        return transactionAmount;
    }
}
