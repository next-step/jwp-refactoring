package kichenpos.order.table.model;

public final class OrderTable {

    private boolean empty;

    public OrderTable(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }
}
