import org.json.*;

/**
 * Created by michalgraczykowski on 22.06.16.
 *
 * Factory design pattern - creates Order object.
 */
public final class OrderFactory {
    /**
     * Creates BuyOrder object based on JSON object.
     *
     * Input examples:
     * {“type”:“Limit”,“order”:{“direction”:“Buy”,“id”:1,“price”:14,“quantity”:20}}
     * {“type”:“Iceberg”,“order”:{“direction”:“Buy”,“id”:2,“price”:15,“quantity”:50,“peak”:20}}
     *
     * Assumes that the input data is correct.
     *
     * @param jsonObject input data
     * @return buy order object based on input
     */
    public static BuyOrder createBuyOrder (JSONObject jsonObject) throws Exception {
        // Resolve order type
        String type = jsonObject.getString("type");

        // Create order JSON object
        JSONObject orderJSONObject = jsonObject.getJSONObject("order");

        //Resolve order id
        int id = orderJSONObject.getInt("id");

        //Resolve order price
        int price = orderJSONObject.getInt("price");

        //Resolve order quantity
        int quantity = orderJSONObject.getInt("quantity");

        if (type.equals("Limit")) {
            // Return LimitOrder Object
            return new LimitOrder(id, price, quantity);
        } else if (type.equals("Iceberg")) {
            // Resolve order peak
            int peak = orderJSONObject.getInt("peak");

            // Return IcebergOrder Object
            return new IcebergOrder(id, price, quantity, peak);
        } else {
            // Assumes that the input data is correct
            // but does not compile without throwing an exception
            throw new Exception("Inorrect order type");
        }
    }

    /**
     * Creates SellOrder object based on JSON object.
     *
     * Input examples:
     * {“type”:“Limit”,“order”:{“direction”:“Buy”,“id”:1,“price”:14,“quantity”:20}}
     * {“type”:“Iceberg”,“order”:{“direction”:“Buy”,“id”:2,“price”:15,“quantity”:50,“peak”:20}}
     *
     * Assumes that the input data is correct.
     *
     * @param jsonObject input data
     * @return buy order object based on input
     */
    public static SellOrder createSellOrder (JSONObject jsonObject) throws Exception {
        // Resolve order type
        String type = jsonObject.getString("type");

        // Create order JSON object
        JSONObject orderJSONObject = jsonObject.getJSONObject("order");

        //Resolve order id
        int id = orderJSONObject.getInt("id");

        //Resolve order price
        int price = orderJSONObject.getInt("price");

        //Resolve order quantity
        int quantity = orderJSONObject.getInt("quantity");

        if (type.equals("Limit")) {
            // Return LimitOrder Object
            return new LimitOrder(id, price, quantity);
        } else if (type.equals("Iceberg")) {
            // Resolve order peak
            int peak = orderJSONObject.getInt("peak");

            // Return IcebergOrder Object
            return new IcebergOrder(id, price, quantity, peak);
        } else {
            // Assumes that the input data is correct
            // but does not compile without throwing an exception
            throw new Exception("Inorrect order type");
        }
    }
}
