/**
 * Created by michalgraczykowski on 24.06.16.
 *
 * Interface with method to check if
 * transaction between 2 given orders is available.
 */
public interface TransactionCheckerInterface {
    /**
     * Checks if transaction between 2 given orders is available.
     *
     * @param matchingOrder matching Order object
     * @param oppositeOrder opposite Order object
     * @return true if transaction is available, false if not
     */
    boolean isTransactionAvailable(Order matchingOrder, Order oppositeOrder);
}

