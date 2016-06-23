import org.json.JSONObject;

import java.util.Date;

/**
 * Created by michalgraczykowski on 22.06.16.
 *
 * Represents Order.
 */
public abstract class Order {
    protected final String direction;
    protected final int id;
    protected final int price;
    protected int quantity;
    protected Date timeStamp;

    public Order(String direction, int id, int price, int quantity) {
        this.direction = direction;
        this.id = id;
        this.price = price;
        this.quantity = quantity;

        /* timeStamp = current date */
        this.timeStamp = new Date();
    }

    public String getDirection() {
        return this.direction;
    }

    public int getId() {
        return this.id;
    }

    public int getPrice() {
        return this.price;
    }

    public int getQuantity() {
        return this.quantity;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("direction", this.direction);
        jsonObject.put("id", this.id);
        jsonObject.put("price", this.price);
        jsonObject.put("quantity", this.quantity);
        return jsonObject.toString();
    }

    protected abstract void decreaseQuantity(int amount);

    public abstract void runTransaction(Order other);
}

