/**
 * Created by michalgraczykowski on 22.06.16.
 */
public abstract class SellOrder extends Order implements Comparable<SellOrder>{
    public SellOrder(int id, int price, int quantity) {
        super(id, price, quantity);
    }

    /**
     * Compares with another SellOrder object.
     * Order: Ascending on prices,
     * if equal, ascending on timeStamps
     *
     * @param other SellOrder object to compare with
     * @return 0 if equal, > 0 if this > other, < 0 if this < other
     */
    @Override
    public int compareTo(SellOrder other) {
        /* Get the difference in prices */
        int priceDifference = this.price - other.price;

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
