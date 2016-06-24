import java.util.Comparator;
import java.util.TreeSet;
import java.util.Scanner;
import org.json.*;
import static java.lang.Math.max;

/**
 * Created by michalgraczykowski on 22.06.16.
 *
 * Class to simulate Order Book.
 */
public final class OrderBookSimulator {
    /**
     * Comparator for Order objects.
     * Compares on prices
     * if equal, ascending on timeStamps,
     * if equal, ascending on orderIds
     */
    private class OrderComparator implements Comparator<Order> {
        private OrderOnPricesComparatorInterface orderOnPricesComparatorInterface;

        /**
         * Constructor.
         *
         * @param orderOnPricesComparatorInterface interface comparing Order objects on prices
         */
        public OrderComparator(OrderOnPricesComparatorInterface orderOnPricesComparatorInterface) {
            this.orderOnPricesComparatorInterface = orderOnPricesComparatorInterface;
        }

        /**
         * Compares two Order objects.
         *
         * @param o1 first order to compare
         * @param o2 second order to compare
         * @return 0 if equal, >0 if o1>o2, <0 if o1<o2
         */
        @Override
        public int compare(Order o1, Order o2) {
            /* Get the difference in prices */
            int priceDifference = orderOnPricesComparatorInterface.compareOnPrices(o1, o2);
            if (priceDifference != 0) {
                return priceDifference;
            }

            /* Get the difference in timeStamps */
            int timeStampDifference = o1.timeStamp.compareTo(o2.timeStamp);
            if (timeStampDifference != 0) {
                return timeStampDifference;
            }

            /* If prices and timeStamps equal, compare on orderIds */
            return o1.orderId - o2.orderId;
        }
    }


    /* Sorted set of Order objects with "Buy" direction.
       Sorting descending on prices. */
    TreeSet<Order> buyOrders = new TreeSet<>(new OrderComparator(
            (o1, o2) -> {
                return o2.getPrice() - o1.getPrice();
            }
    ));

    /* Sorted set of Order objects with "Sell" direction.
       Sorting ascending on prices. */
    TreeSet<Order> sellOrders = new TreeSet<>(new OrderComparator(
            (o1, o2) -> {
                return o1.getPrice() - o2.getPrice();
            }
    ));


    /**
     * Prints info about the transaction in JSON format.
     */
    private void printTransactionInfo(int buyOrderId, int sellOrderId, int price, int quantity) {
        JSONObject transactionJSON = new JSONObject();
        transactionJSON.put("buyOrderId", buyOrderId);
        transactionJSON.put("sellOrderId", sellOrderId);
        transactionJSON.put("price", price);
        transactionJSON.put("quantity", quantity);
        System.out.println(transactionJSON.toString());
    }

    private void addToOrderBook(Order newOrder, TreeSet<Order> matchingOrdersSet,
                                TreeSet<Order> oppositeOrdersSet,
                                TransactionCheckerInterface transactionCheckerInterface,
                                TransactionPrinterInterface transactionPrinterInterface) {
        /* Knows if we should continue transactions */
        boolean continueTransactions = true;

        /* Main loop - run as many transactions as possible */
        while (oppositeOrdersSet.size() > 0 && continueTransactions) {
            /* Knows if order with possible transaction was found */
            boolean foundMatchingOrder;

            /* Resolve first order from the opposite orders */
            Order firstOppositeOrder = oppositeOrdersSet.first();

            if (transactionCheckerInterface.isTransactionAvailable(newOrder, firstOppositeOrder)) {
                /* Mark that order with possible transaction was found */
                foundMatchingOrder = true;

                /* Remove order with possible transaction from its set */
                oppositeOrdersSet.remove(firstOppositeOrder);

                /* Run transaction and resolve its quantity */
                int quantity = firstOppositeOrder.runTransaction(newOrder);
                /* to fill!! */
                int matchingOrderId = newOrder.getId();
                /* Resolve opposite order id */
                int oppositeOrderId = firstOppositeOrder.getId();
                /* Resolve transaction price */
                int price = max(newOrder.getPrice(), firstOppositeOrder.getPrice());
                /* Print transaction info */
                transactionPrinterInterface.printTransactionInfo(matchingOrderId, oppositeOrderId,
                        price, quantity);
            } else {
                /* Mark that order with possible transaction was not found */
                foundMatchingOrder = false;
            }

            /* Knows if newOrder is done */
            boolean newOrderIsDone = false;

            if (foundMatchingOrder) {
                /* If the opposite order is not done
                we need to put it back to its set */
                if (firstOppositeOrder.getQuantity() > 0) {
                    oppositeOrdersSet.add(firstOppositeOrder);
                }
                /* Mark if newOrder is done */
                newOrderIsDone = (newOrder.getQuantity() == 0);
            }

            /* Conjunction of two operands */
            continueTransactions = (foundMatchingOrder && !newOrderIsDone);
        }

        /* In newOrder is not done put it into its set */
        if (newOrder.getQuantity() > 0) {
            matchingOrdersSet.add(newOrder);
        }
    }

    private void addSellOrder(Order newSellOrder) {
        addToOrderBook(newSellOrder, sellOrders, buyOrders,
                (sellOrder, buyOrder) -> {
                    return (sellOrder.getPrice() <= buyOrder.getPrice());
                },
                (sellOrderId, buyOrderId, price, quantity) -> {
                    printTransactionInfo(buyOrderId, sellOrderId, price, quantity);
                });
    }

    private void addBuyOrder(Order newBuyOrder) {
        addToOrderBook(newBuyOrder, buyOrders, sellOrders,
                (buyOrder, sellOrder) -> {
                    return (buyOrder.getPrice() >= sellOrder.getPrice());
                },
                (buyOrderId, sellOrderId, price, quantity) -> {
                    printTransactionInfo(buyOrderId, sellOrderId, price, quantity);
                });
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
            addBuyOrder(newOrder);
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