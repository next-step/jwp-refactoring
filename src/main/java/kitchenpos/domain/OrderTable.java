package kitchenpos.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private TableGroup tableGroup;
    @Embedded
    private NumberOfGuests numberOfGuests;
    private boolean empty;
    @OneToMany
    @JoinColumn(name = "orderTableId")
    private List<Order> orders = new ArrayList<>();

    public OrderTable() {
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty, List<Order> orders) {
        this(id, tableGroup, numberOfGuests, empty);
        this.orders = orders;
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = NumberOfGuests.of(numberOfGuests);
        this.empty = empty;
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = NumberOfGuests.of(numberOfGuests);
        this.empty = empty;
    }

    public OrderTable(int numberOfGuests) {
        this.numberOfGuests = NumberOfGuests.of(numberOfGuests);
    }

    public OrderTable(boolean empty) {
        this.empty = empty;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        if (isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.numberOfGuests = NumberOfGuests.of(numberOfGuests);
    }

    public void changeEmptyStatus(final boolean empty) {
        this.empty = empty;
    }

    public void changeToNotEmpty() {
        this.empty = false;
    }

    public boolean hasTableGroup() {
        return tableGroup != null;
    }

    public boolean isOrderStarted() {
        for (Order order : orders) {
            if (order.isStarted()) {
                return true;
            }
        }

        return false;
    }

    public void setTableGroup(TableGroup tableGroup) {
        if (!isEmpty() || hasTableGroup()) {
            throw new IllegalArgumentException();
        }
        this.tableGroup = tableGroup;
    }

    public void unsetTableGroup() {
        this.tableGroup = null;
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.value();
    }

    public boolean isEmpty() {
        return empty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderTable that = (OrderTable) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
