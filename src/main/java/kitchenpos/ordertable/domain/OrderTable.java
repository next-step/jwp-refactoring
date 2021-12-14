package kitchenpos.ordertable.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.tablegroup.domain.TableGroup;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private TableGroup tableGroup;

    @Column(nullable = false)
    private int numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    @Embedded
    private Orders orders = new Orders();

    protected OrderTable() {
    }

    public OrderTable(TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(boolean empty) {
        this.empty = empty;
    }

    public OrderTable(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
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

    public Orders getOrders() {
        return orders;
    }

    public void changeTableGroup(TableGroup tableGroup) {
        tableGroup.addToOrderTables(this);
        this.tableGroup = tableGroup;
    }

    public void changeEmpty(boolean empty) {
        validateChangingEmpty();
        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        validateChangingNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    public void ungroup() {
        tableGroup = null;
    }

    public boolean isGroupable() {
        return empty && Objects.isNull(tableGroup);
    }

    public boolean isChangable() {
        return orders.isChangable();
    }

    public void addToOrders(Order order) {
        orders.add(order);
    }

    private void validateChangingEmpty() {
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException();
        }
        orders.validateChangingEmpty();
    }

    private void validateChangingNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
        if (empty) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderTable that = (OrderTable) o;
        return numberOfGuests == that.numberOfGuests && empty == that.empty && Objects.equals(id, that.id) && Objects.equals(tableGroup, that.tableGroup) && Objects.equals(orders, that.orders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tableGroup, numberOfGuests, empty, orders);
    }
}
