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

    @ManyToOne(fetch = FetchType.LAZY)
    private TableGroup tableGroup;

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

    public OrderTable(TableGroup tableGroup, Orders orders, NumberOfGuest numberOfGuest, boolean empty) {
        this(null, tableGroup, orders, numberOfGuest, empty);
    }

    public OrderTable(Long id, NumberOfGuest numberOfGuests, boolean empty) {
        this(id, null, Collections.emptyList(), numberOfGuests, empty);
    }

    public OrderTable(Long id, TableGroup tableGroup, List<Order> orders, NumberOfGuest numberOfGuests, boolean empty) {
        this(id, tableGroup, new Orders(orders), numberOfGuests, empty);
    }

    public OrderTable(Long id, TableGroup tableGroup, Orders orders, NumberOfGuest numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.orders = orders;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void ungroup() {
        if (!isUnGroupable()) {
            throw new IllegalStateException();
        }

        this.tableGroup = null;
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

    public void bookedBy(TableGroup tableGroup) {
        if (isBooked()) {
            throw new IllegalStateException();
        }

        this.tableGroup = tableGroup;
        this.empty = false;
    }

    public boolean isBooked() {
        return Objects.nonNull(getTableGroup()) || !isEmpty();
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

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public boolean isEmpty() {
        return empty;
    }
}
