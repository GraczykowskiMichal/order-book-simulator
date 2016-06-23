import java.util.Comparator;
import java.util.TreeSet;
import java.util.Scanner;
import org.json.*;

/**
 * Created by michalgraczykowski on 22.06.16.
 */
public final class OrderBookSimulator {
    /**
     * Comparator for Order objects with "Buy" direction.
     * Order: Descending on prices,
     * if equal, ascending on timeStamps
     */
    private class BuyOrderComparator implements Comparator<Order> {

        /**
         * Compares two Order objects with "Buy" direction.
         *
         * @param o1 first order to compare
         * @param o2 second order to compare
         * @return 0 if equal, > 0 if o1 > o2, < 0 if o1 < o2
         */
        @Override
        public int compare(Order o1, Order o2) {
            /* Get the difference in prices */
            int priceDifference = o2.price - o1.price;

            if (priceDifference != 0) {
                return priceDifference;
            } else {
                return o1.timeStamp.compareTo(o2.timeStamp);
            }
        }
    }

    /**
     * Comparator for Order objects with "Sell" direction.
     * Order: Ascending on prices,
     * if equal, ascending on timeStamps
     */
    private class SellOrderComparator implements Comparator<Order> {

        /**
         * Compares two Order objects with "Sell" direction.
         *
         * @param o1 first order to compare
         * @param o2 second order to compare
         * @return 0 if equal, > 0 if o1 > o2, < 0 if o1 < o2
         */
        @Override
        public int compare(Order o1, Order o2) {
            /* Get the difference in prices */
            int priceDifference = o1.price - o2.price;

            if (priceDifference != 0) {
                return priceDifference;
            } else {
                return o1.timeStamp.compareTo(o2.timeStamp);
            }
        }
    }


    /* Sorted set of Order objects with "Buy" direction */
    TreeSet<Order> buyOrders = new TreeSet<Order>(new BuyOrderComparator());

    /* Sorted set of Order objects with "Sell" direction */
    TreeSet<Order> sellOrders = new TreeSet<Order>(new SellOrderComparator());


    private void addSellOrder(Order newSellOrder) {
        boolean continueTransactions = true;
        while (buyOrders.size() > 0 && continueTransactions) {
            boolean foundMatchingOrder;
            Order firstBuyOrder = buyOrders.first();
            if (firstBuyOrder.getPrice() >= newSellOrder.getPrice()) {
                foundMatchingOrder = true;
                buyOrders.remove(firstBuyOrder);
                firstBuyOrder.runTransaction(newSellOrder);
            } else {
                foundMatchingOrder = false;
            }

            if (foundMatchingOrder) {
                if (firstBuyOrder.getQuantity() > 0) {
                    buyOrders.add(firstBuyOrder);
                }
                continueTransactions = (newSellOrder.getQuantity() > 0);
            }
            continueTransactions = continueTransactions && foundMatchingOrder;
        }

        if (newSellOrder.getQuantity() > 0) {
            sellOrders.add(newSellOrder);
        }
    }

    private void addBuyOrder(Order newOrder) {

    }

    /**
     * Create order from the JSON input
     * and processes it.
     *
     * Assumes that the input data is correct.
     *
     * @param input Data in JSON format to create order
     */
    private void processOrder(String input) {
        /* Create new Order object */
        Order newOrder = OrderFactory.createOrder(input);

        /* Append to proper collection */
        String direction = newOrder.getDirection();
        if (direction.equals("Buy")) {
            //addBuyOrder(newOrder);
            buyOrders.add(newOrder);
        } else if (direction.equals("Sell")) {
            addSellOrder(newOrder);
        }

        System.out.println("Buy orders:");
        for (Order order : buyOrders) {
            System.out.println(order);
        }

        System.out.println("Sell orders:");
        for (Order order : sellOrders) {
            System.out.println(order);
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

        /* Main loop - read and process each line until the end of input */
        while (scanner.hasNextLine()) {
            processOrder(scanner.nextLine());
        }
    }
}