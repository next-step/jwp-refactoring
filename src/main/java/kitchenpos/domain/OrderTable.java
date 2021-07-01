package kitchenpos.domain;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private TableGroup tableGroup;

    private Orders orders = new Orders();

    private NumberOfGuest numberOfGuests;

    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(TableGroup tableGroup, Orders orders, int numberOfGuests, boolean empty) {
        this(null, tableGroup, orders, new NumberOfGuest(numberOfGuests), empty);
    }

    public OrderTable(TableGroup tableGroup, List<Order> orders, int numberOfGuests, boolean empty) {
        this(null, tableGroup, new Orders(orders), new NumberOfGuest(numberOfGuests), empty);
    }

    public OrderTable(Long id, TableGroup tableGroup, Orders orders, int numberOfGuests, boolean empty) {
        this(id, tableGroup, orders, new NumberOfGuest(numberOfGuests), empty);
    }

    public OrderTable(Long id, TableGroup tableGroup, List<Order> orders, int numberOfGuests, boolean empty) {
        this(id, tableGroup, new Orders(orders), new NumberOfGuest(numberOfGuests), empty);
    }

    public OrderTable(Long id, TableGroup tableGroup, Orders orders, NumberOfGuest numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.orders = orders;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(NumberOfGuest numberOfGuest, boolean empty) {
        this.numberOfGuests = numberOfGuest;
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public List<Order> getOrders() {
        return orders.toCollection();
    }

    public NumberOfGuest getNumberOfGuests() {
        return numberOfGuests;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void ungroup() {
        if (!isUnGroupable()) {
            throw new IllegalStateException();
        }
    }

    public boolean isUnGroupable() {
        return orders.isAllFinished();
    }

    public void changeNumberOfGuest(NumberOfGuest numberOfGuest) {
        if (empty) {
            throw new IllegalStateException();
        }

        this.numberOfGuests = numberOfGuest;
    }

    public void changeEmpty(boolean empty) {
        if (isBooked()) {
            throw new IllegalArgumentException();
        }

        if (!orders.isAllFinished()) {
            throw new IllegalStateException();
        }

        this.empty = empty;
    }

    public boolean isBooked() {
        return Objects.nonNull(getTableGroup()) || !isEmpty();
    }

    public void bookedBy(TableGroup tableGroup) {
        if (isBooked()) {
            throw new IllegalStateException();
        }

        this.tableGroup = tableGroup;
        this.empty = false;
    }
}
