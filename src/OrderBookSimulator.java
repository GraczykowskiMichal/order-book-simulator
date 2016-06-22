import java.util.TreeSet;
import java.util.Scanner;
import org.json.*;

/**
 * Created by michalgraczykowski on 22.06.16.
 */
public final class OrderBookSimulator {
    /* Sorted set of BuyOrder objects */
    TreeSet<BuyOrder> buyOrders = new TreeSet<BuyOrder>();

    /* Sorted set of SellOrder objects */
    TreeSet<SellOrder> sellOrders = new TreeSet<SellOrder>();

    /**
     * Decides where to append order created from the input
     * and appends to the proper collection (buyOrders or sellOrders).
     *
     * Assumes that the input data is correct.
     *
     *  @param input Data in JSON format to create order
     */
    private void appendOrder(String input) {
        /* Replace double quotes (“ and ”) with the single quote (') */
        String jsonInput = input.replace("“", "'").replace("”", "'");

        /* Create JSON object based on input */
        JSONObject jsonObject = new JSONObject(jsonInput);

        /* Resolve order direction */
        String direction = jsonObject.getJSONObject("order").getString("direction");

        try {
            /* Append to proper collection */
            if (direction.equals("Buy")) {
                buyOrders.add(OrderFactory.createBuyOrder(jsonObject));
            } else if (direction.equals("Sell")) {
                sellOrders.add(OrderFactory.createSellOrder(jsonObject));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Simulates the Order Book transactions.
     *
     * Reads data from the standard input,
     * creates transactions objects based on input,
     * manage transactions in the priority queue
     * and writes info to the standard output.
     *
     * Input line examples:
     * {“type”:“Limit”,“order”:{“direction”:“Buy”,“id”:1,“price”:14,“quantity”:20}}
     * {“type”:“Iceberg”,“order”:{“direction”:“Buy”,“id”:2,“price”:15,“quantity”:50,“peak”:20}}
     */
    public void run() {
        /* Create a scanner so we can read the input */
        Scanner scanner = new Scanner(System.in);

        /* Main loop - read each line until the end of input */
        while (scanner.hasNextLine()) {
            try {
                appendOrder(scanner.nextLine());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
