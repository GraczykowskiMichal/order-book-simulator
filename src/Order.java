import org.json.JSONObject;

import java.util.Date;

import static java.lang.Math.min;

/**
 * Created by michalgraczykowski on 22.06.16.
 *
 * Abstract representation of Order.
 */
public abstract class Order {
    protected final String direction;
    protected final int id;
    protected final int price;
    protected int quantity;
    protected Date timeStamp;

    /* id to know incoming order
       if the prices and timeStamps are equal */
    protected final int orderId;

    /**
     * Constructor simply assigning values.
     * Assumes that the input data is correct.
     */
    public Order(String direction, int id, int price, int quantity, int orderId) {
        this.direction = direction;
        this.id = id;
        this.price = price;
        this.quantity = quantity;
        /* timeStamp = current date */
        this.timeStamp = new Date();
        this.orderId = orderId;
    }

    /**
     * @return direction of the Order
     */
    public String getDirection() {
        return this.direction;
    }

    /**
     * @return id of the Order
     */
    public int getId() {
        return this.id;
    }

    /**
     * @return price of the Order
     */
    public int getPrice() {
        return this.price;
    }

    /**
     * @return quantity of the Order
     */
    public int getQuantity() {
        return this.quantity;
    }

    /**
     * Converts order to ordered JSON string.
     *
     * @return Ordered JSON string with fields: id, price, quantity
     */
    public String toJSONString() {
        StringBuilder orderedJSONStringBuilder = new StringBuilder();

        orderedJSONStringBuilder.append("{“id”: ");
        orderedJSONStringBuilder.append(this.getId());

        orderedJSONStringBuilder.append(", “price”: ");
        orderedJSONStringBuilder.append(this.getPrice());

        orderedJSONStringBuilder.append(", “quantity”: ");
        orderedJSONStringBuilder.append(this.getQuantity());
        orderedJSONStringBuilder.append("}");

        return orderedJSONStringBuilder.toString();
    }

    /**
     * Decreases quantity of the Order.
     * Assumes that amount <= this.quantity.
     *
     * @param amount amount to decrease
     */
    protected abstract void decreaseQuantity(int amount);

    /**
     * Runs transaction on the Order
     * assuming that this method is called
     * on the Order that just joined the Order Book.
     * Assumes that transaction can be made.
     *
     * @param other Order to make the transaction with
     * @return amount of the transaction
     */
    public int runTransaction(Order other) {
        int transactionAmount = min(this.getQuantity(), other.getQuantity());
        this.decreaseQuantity(transactionAmount);
        other.decreaseQuantity(transactionAmount);
        return transactionAmount;
    }
}
