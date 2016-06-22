import java.util.Comparator;

/**
 * Created by michalgraczykowski on 22.06.16.
 */
public abstract class BuyOrder extends Order implements Comparable<BuyOrder> {
    public BuyOrder(int id, int price, int quantity) {
        super(id, price, quantity);
    }

    /**
     * Compares with another BuyOrder object.
     * Order: Descending on prices,
     * if equal, ascending on timeStamps
     *
     * @param other BuyOrder object to compare with
     * @return 0 if equal, > 0 if this > other, < 0 if this < other
     */
    @Override
    public int compareTo(BuyOrder other) {
        /* Get the difference in prices */
        int priceDifference = other.price - this.price;

        if (priceDifference != 0) {
            return priceDifference;
        } else {
            return this.timeStamp.compareTo(other.timeStamp);
        }
    }

    @Override
    public String toString() {
        return Integer.toString(this.price);
    }
}
