package kitchenpos.order.domain;

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

    private int numberOfGuests;

    private boolean empty;

    protected OrderTable() {

    }

    public OrderTable(Long id, int numberOfGuests, boolean empty) {
        this(numberOfGuests, empty);
        this.id = id;
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.tableGroup = null;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void checkOrderTableIsEmpty() {
        if (!empty) {
            throw new IllegalArgumentException();
        }
    }

    public void updateTableGroup(TableGroup tableGroup) {
        if (this.tableGroup != tableGroup) {
            this.tableGroup = tableGroup;
            tableGroup.addOrderTable(this);
        }
    }

    public void changeEmpty(boolean empty, List<Order> orders) {
        if (Objects.nonNull(this.tableGroup)) {
            throw new IllegalArgumentException();
        }
        orders.forEach(Order::checkOngoingOrderTable);
        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (empty) {
            throw new IllegalArgumentException();
        }
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
        this.numberOfGuests = numberOfGuests;
    }

    public void ungroup() {
        this.tableGroup = null;
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
