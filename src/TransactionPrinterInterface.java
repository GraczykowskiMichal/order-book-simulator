/**
 * Created by michalgraczykowski on 24.06.16.
 */
public interface TransactionPrinterInterface {
    /**
     * Processes info about transaction.
     *
     * @param matchingOrderId id of the matching Order object
     * @param oppositeOrderId id of the opposite Order object
     * @param price price of the transaction
     * @param quantity quantity of the transaction
     */
    void processTransactionInfo(int matchingOrderId, int oppositeOrderId, int price, int quantity);
}
