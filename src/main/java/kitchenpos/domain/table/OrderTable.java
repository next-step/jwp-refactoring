package kitchenpos.domain.table;

import kitchenpos.domain.NumberOfGuest;
import kitchenpos.domain.menu.Menus;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderCreate;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.Orders;
import kitchenpos.exception.TableEmptyException;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_group_id")
    private Long tableGroupId;

    private Orders orders = new Orders();

    private NumberOfGuest numberOfGuests;

    private boolean empty;

    public static OrderTable from(OrderTableCreate create) {
        return new OrderTable(create.getNumberOfGuests(), create.isEmpty());
    }

    public static Order newOrder(OrderTable orderTable, OrderCreate orderCreate, Menus menus) {
        if (orderTable.isEmpty()) {
            throw new TableEmptyException();
        }

        orderCreate = orderCreate.changeOrderStatus(OrderStatus.COOKING);

        return Order.createOrder(orderTable.getId(), orderCreate, menus);
    }

    protected OrderTable() {
    }

    public OrderTable(NumberOfGuest numberOfGuest, boolean empty) {
        this(null, null, Collections.emptyList(), numberOfGuest, empty);
    }

    public OrderTable(Long tableGroupId, Orders orders, NumberOfGuest numberOfGuest, boolean empty) {
        this(null, tableGroupId, orders, numberOfGuest, empty);
    }

    public OrderTable(Long id, NumberOfGuest numberOfGuests, boolean empty) {
        this(id, null, Collections.emptyList(), numberOfGuests, empty);
    }

    public OrderTable(Long id, Long tableGroupId, List<Order> orders, NumberOfGuest numberOfGuests, boolean empty) {
        this(id, tableGroupId, new Orders(orders), numberOfGuests, empty);
    }

    public OrderTable(Long id, Long tableGroupId, Orders orders, NumberOfGuest numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.orders = orders;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void ungroup() {
        if (!isUnGroupable()) {
            throw new IllegalStateException();
        }

        this.tableGroupId = null;
    }

    public void changeNumberOfGuest(NumberOfGuest numberOfGuest) {
        if (empty) {
            throw new IllegalStateException();
        }

        this.numberOfGuests = numberOfGuest;
    }

    public void changeEmpty(boolean empty) {
        if (!orders.isAllFinished()) {
            throw new IllegalStateException();
        }

        if (isBooked()) {
            throw new IllegalArgumentException();
        }

        this.empty = empty;
    }

    public void bookedBy(Long tableGroupId) {
        if (isBooked()) {
            throw new IllegalStateException();
        }

        this.tableGroupId = tableGroupId;
        this.empty = false;
    }

    public boolean isBooked() {
        return Objects.nonNull(getTableGroupId()) || !isEmpty();
    }

    public boolean isUnGroupable() {
        return orders.isAllFinished();
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

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public boolean isEmpty() {
        return empty;
    }
}
