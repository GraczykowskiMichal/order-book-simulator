/**
 * Created by michalgraczykowski on 22.06.16.
 *
 * Represents Limit Order.
 */
public abstract class LimitOrder extends Order {
    public LimitOrder(int id, int price, int quantity) {
        super(id, price, quantity);
    }
}
