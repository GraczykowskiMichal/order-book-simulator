import org.json.JSONObject;

/**
 * Created by michalgraczykowski on 22.06.16.
 *
 * Represents Iceberg Order.
 */
public class IcebergOrder extends Order {
    protected final int peak;

    public IcebergOrder(String direction, int id, int price, int quantity, int peak) {
        super(direction, id, price, quantity);
        this.peak = peak;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("direction", this.direction);
        jsonObject.put("id", this.id);
        jsonObject.put("price", this.price);
        jsonObject.put("quantity", this.quantity);
        jsonObject.put("peak", this.peak);
        return jsonObject.toString();
    }

    @Override
    protected void decreaseQuantity(int amount) {

    }

    @Override
    public void runTransaction(Order other) {

    }
}
