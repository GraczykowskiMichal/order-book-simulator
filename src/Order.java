import java.util.Date;

/**
 * Created by michalgraczykowski on 22.06.16.
 */
public abstract class Order {
    protected final int id;
    protected final int price;
    protected int quantity;
    protected Date timeStamp;

    public Order(int id, int price, int quantity) {
        this.id = id;
        this.price = price;
        this.quantity = quantity;
        this.timeStamp = new Date();
    }
}

