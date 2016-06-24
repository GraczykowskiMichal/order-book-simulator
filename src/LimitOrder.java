import org.json.JSONObject;

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
    protected void decreaseQuantity(int amount) {
        this.quantity -= amount;
    }
}
