import java.util.Comparator;
import java.util.LinkedList;
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
     * if equal, ascending on timeStamps,f
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
         * @param order1 first order to compare
         * @param order2 second order to compare
         * @return 0 if equal, >0 if order1>order2, <0 if order1<order2
         */
        @Override
        public int compare(Order order1, Order order2) {
            /* Get the difference in prices */
            int priceDifference = orderOnPricesComparatorInterface.compareOnPrices(order1, order2);
            if (priceDifference != 0) {
                return priceDifference;
            }

            /* Get the difference in timeStamps */
            int timeStampDifference = order1.timeStamp.compareTo(order2.timeStamp);
            if (timeStampDifference != 0) {
                return timeStampDifference;
            }

            /* If prices and timeStamps equal, compare on orderIds */
            return order1.orderId - order2.orderId;
        }
    }


    /* Sorted set of Order objects with "Buy" direction.
       Sorting descending on prices. */
    TreeSet<Order> buyOrders = new TreeSet<>(new OrderComparator(
            (order1, order2) -> {
                return order2.getPrice() - order1.getPrice();
            }
    ));

    /* Sorted set of Order objects with "Sell" direction.
       Sorting ascending on prices. */
    TreeSet<Order> sellOrders = new TreeSet<>(new OrderComparator(
            (order1, order2) -> {
                return order1.getPrice() - order2.getPrice();
            }
    ));

    /* List of transactions' info in JSON format */
    LinkedList<String> transactionInfoList = new LinkedList<>();


    /**
     * Prints info about orders in the Order Book
     */
    private void printOrdersInfo() {
        System.out.println("Buy orders:");
        for (Order order : buyOrders) {
            System.out.println(order);
        }

        System.out.println();

        System.out.println("Sell orders:");
        for (Order order : sellOrders) {
            System.out.println(order);
        }

        System.out.println();
    }

    /**
     * Adds transaction's info to the transactionInfoList.
     *
     * @param buyOrderId buy Order's id
     * @param sellOrderId sell Order's id
     * @param price price of the transaction
     * @param quantity quantity of the transaction
     */
    private void addToTransactionInfoList(int buyOrderId, int sellOrderId, int price, int quantity) {
        /* Create JSON object and fill it */
        JSONObject transactionJSON = new JSONObject();
        transactionJSON.put("buyOrderId", buyOrderId);
        transactionJSON.put("sellOrderId", sellOrderId);
        transactionJSON.put("price", price);
        transactionJSON.put("quantity", quantity);

        /* Append new info */
        transactionInfoList.add(transactionJSON.toString());
    }

    /**
     * Prints info in the transactionInfoList and clears it.
     */
    private void printTransactionInfoList() {
        /* Print info */
        for (String transactionInfo : transactionInfoList) {
            System.out.println(transactionInfo);
        }

        /* Clear list */
        transactionInfoList.clear();
    }

    /**
     * Executes possible transactions with new Order,
     * and if it is not finished, adds it to the proper collection.
     *
     * @param newOrder Order to add to Order Book
     * @param matchingOrdersSet if Order is buy order then buyOrders, if sell order then sellOrders
     * @param oppositeOrdersSet if Order is buy order then sellOrders, if sell order then buyOrders
     * @param transactionCheckerInterface interface to check if transaction is possible
     * @param transactionPrinterInterface interface to print transaction's info
     */
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

            /* Get first order from the opposite orders */
            Order firstOppositeOrder = oppositeOrdersSet.first();

            if (transactionCheckerInterface.isTransactionAvailable(newOrder, firstOppositeOrder)) {
                /* Mark that order with possible transaction was found */
                foundMatchingOrder = true;

                /* Remove order with possible transaction from its set */
                oppositeOrdersSet.remove(firstOppositeOrder);

                /* Run transaction and Get its quantity */
                int quantity = firstOppositeOrder.runTransaction(newOrder);
                /* Get new Order id*/
                int matchingOrderId = newOrder.getId();
                /* Get first opposite Order id */
                int oppositeOrderId = firstOppositeOrder.getId();
                /* Get transaction price */
                int price = max(newOrder.getPrice(), firstOppositeOrder.getPrice());
                /* Print transaction info */
                transactionPrinterInterface.processTransactionInfo(matchingOrderId, oppositeOrderId,
                        price, quantity);
            } else {
                /* Mark that order with possible transaction was not found */
                foundMatchingOrder = false;
            }

            /* Knows if newOrder is finished */
            boolean newOrderIsFinished = false;

            if (foundMatchingOrder) {
                /* If the first opposite order is not finished
                we need to put it back to its set */
                if (firstOppositeOrder.getQuantity() > 0) {
                    oppositeOrdersSet.add(firstOppositeOrder);
                }
                /* Mark if newOrder is finished */
                newOrderIsFinished = (newOrder.getQuantity() == 0);
            }

            /* Conjunction of two operands */
            continueTransactions = (foundMatchingOrder && !newOrderIsFinished);
        }

        /* In newOrder is not finished put it into its set */
        if (newOrder.getQuantity() > 0) {
            matchingOrdersSet.add(newOrder);
        }
    }

    /**
     * Processes adding new sell Order to Order Book
     *
     * @param newSellOrder order to add to Order Book
     */
    private void addSellOrder(Order newSellOrder) {
        /* Add to Order Book */
        addToOrderBook(newSellOrder, sellOrders, buyOrders,
                /* Determine when transaction is possible */
                (sellOrder, buyOrder) -> {
                    return (sellOrder.getPrice() <= buyOrder.getPrice());
                },
                /* Process transaction info - add it to the list to print later */
                (sellOrderId, buyOrderId, price, quantity) -> {
                    addToTransactionInfoList(buyOrderId, sellOrderId, price, quantity);
                });
    }

    /**
     * Processes adding new buy Order to Order Book
     *
     * @param newBuyOrder order to add to Order Book
     */
    private void addBuyOrder(Order newBuyOrder) {
        /* Add to Order Book */
        addToOrderBook(newBuyOrder, buyOrders, sellOrders,
                /* Determine when transaction is possible */
                (buyOrder, sellOrder) -> {
                    return (buyOrder.getPrice() >= sellOrder.getPrice());
                },
                /* Process transaction info - add it to the list to print later */
                (buyOrderId, sellOrderId, price, quantity) -> {
                    addToTransactionInfoList(buyOrderId, sellOrderId, price, quantity);
                });
    }

    /**
     * Create order from the JSON input and processes it.
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

        /* Print orders in Order Book */
        printOrdersInfo();

        /* Print list of transactions done */
        printTransactionInfoList();
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