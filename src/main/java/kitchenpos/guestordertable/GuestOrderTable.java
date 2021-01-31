package kitchenpos.guestordertable;

public class GuestOrderTable {
    private long id;
    private boolean empty;

    public GuestOrderTable(long id, boolean empty) {
        if (empty) {
            throw new IllegalArgumentException("This table is empty");
        }
        this.id = id;
        this.empty = false;
    }

    public long getId() {
        return id;
    }
}
