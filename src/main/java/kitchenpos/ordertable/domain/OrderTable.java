package kitchenpos.ordertable.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.tablegroup.domain.TableGroup;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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

    @Column(nullable = false)
    private int numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    @OneToMany(mappedBy = "orderTable")
    private List<Order> orders = new ArrayList<>();

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

    public List<Order> getOrders() {
        return orders;
    }

    public void changeTableGroup(TableGroup tableGroup) {
        tableGroup.getOrderTables().add(this);
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

    private void validateChangingEmpty() {
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException();
        }
        orders.forEach(this::validateOrder);
    }

    private void validateChangingNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
        if (empty) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrder(Order order) {
        if (!order.isChangable()) {
            throw new IllegalArgumentException();
        }
    }

    public boolean isChangable() {
        return orders.stream()
                .allMatch(Order::isChangable);
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
