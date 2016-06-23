import org.json.*;

/**
 * Created by michalgraczykowski on 22.06.16.
 *
 * Factory design pattern.
 * createOrder static method creates Order object based on JSON input.
 */
public final class OrderFactory {
    /**
     * Creates Order object based on JSON input.
     *
     * Input examples:
     * {“type”:“Limit”,“order”:{“direction”:“Buy”,“id”:1,“price”:14,“quantity”:20}}
     * {“type”:“Iceberg”,“order”:{“direction”:“Buy”,“id”:2,“price”:15,“quantity”:50,“peak”:20}}
     *
     * Assumes that the input data is correct.
     *
     * @param input input in JSON format
     * @return Order object based on the input
     */
    public static Order createOrder (String input) {
        /* Replace double quotes (“ and ”) with the single quote (') */
        String jsonInput = input.replace("“", "'").replace("”", "'");

        /* Create JSON object based on input */
        JSONObject jsonObject = new JSONObject(jsonInput);

        /* Resolve order direction */
        String direction = jsonObject.getJSONObject("order").getString("direction");

        /* Resolve order type */
        String type = jsonObject.getString("type");

        /* Create order JSON object */
        JSONObject orderJSONObject = jsonObject.getJSONObject("order");

        /* Resolve order id */
        int id = orderJSONObject.getInt("id");

        /* Resolve order price */
        int price = orderJSONObject.getInt("price");

        /* Resolve order quantity */
        int quantity = orderJSONObject.getInt("quantity");

        if (type.equals("Limit")) {
            /* Return LimitOrder Object */
            return new LimitOrder(direction, id, price, quantity);
        } else {
            /* Assuming that input is correct. */
            /* if type != "Limit" then it equals to "Iceberg" */

            /* Resolve order peak */
            int peak = orderJSONObject.getInt("peak");

            /* Return IcebergOrder Object */
            return new IcebergOrder(direction, id, price, quantity, peak);
        }
    }
}
