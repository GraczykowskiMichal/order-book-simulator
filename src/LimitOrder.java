import org.json.JSONObject;

import static java.lang.Math.min;

/**
 * Created by michalgraczykowski on 22.06.16.
 *
 * Represents Limit Order.
 */
public class LimitOrder extends Order {
    public LimitOrder(String direction, int id, int price, int quantity) {
        super(direction, id, price, quantity);
    }

    @Override
    protected void decreaseQuantity(int amount) {
        this.quantity -= amount;
    }

    @Override
    public void runTransaction(Order other) {
        int amountToDecrease = min(this.quantity, other.quantity);
        this.decreaseQuantity(amountToDecrease);
        other.decreaseQuantity(amountToDecrease);
    }
}
