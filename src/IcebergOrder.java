/**
 * Created by michalgraczykowski on 22.06.16.
 *
 * Represents Iceberg Order.
 */
public abstract class IcebergOrder extends Order {
    protected final int peak;

    public IcebergOrder(int id, int price, int quantity, int peak) {
        super(id, price, quantity);
        this.peak = peak;
    }
}
