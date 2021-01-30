package kitchenpos.ordertable.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    private int numberOfGuests;

    private boolean empty;

    @OneToMany
    @JoinColumn(name = "order_table_id", nullable = false)
    private List<Order> orders = new ArrayList<>();

    protected OrderTable() {
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
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

    public void changeEmpty(final boolean empty) {
        if (isAlreadyGroup()) {
            throw new IllegalArgumentException("지정된 단체 지정이 있습니다.");
        }

        if (hasCookingOrMealOrder()) {
            throw new IllegalArgumentException();
        }

        this.empty = empty;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님 수는 0 이하를 입력할 수 없습니다.");
        }
    }

    protected boolean hasCookingOrMealOrder() {
        return orders.stream().anyMatch(Order::isCookingOrMeal);
    }

    public void putToTableGroup(final TableGroup tableGroup) {
        this.changeEmpty(false);
        this.tableGroup = tableGroup;
    }

    public Long getTableGroupId() {
        if (tableGroup == null) {
            return null;
        }
        return this.tableGroup.getId();
    }

    public boolean isOccupied() {
        return !this.empty;
    }

    public boolean isAlreadyGroup() {
        return this.tableGroup != null;
    }

    public void ungroup() {
        this.tableGroup.removeOrderTable(this);
        this.tableGroup = null;
    }

    public void cooking() {
        this.orders.forEach(Order::cooking);
    }

    public Order order(final List<OrderLineItem> orderLineItems) {
        Order order = Order.createToCook(this, orderLineItems);
        this.orders.add(order);
        return order;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final OrderTable that = (OrderTable)o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
