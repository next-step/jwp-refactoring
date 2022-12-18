package kitchenpos.ordertable.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.order.domain.Order;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static kitchenpos.common.ErrorMessage.*;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;
    @JsonIgnore
    @OneToMany(mappedBy = "orderTable")
    private final List<Order> orders = new ArrayList<>();
    private int numberOfGuests;
    private boolean empty;

    public OrderTable() {
    }

    private OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable of(int numberOfGuests, boolean empty) {
        return new OrderTable(numberOfGuests, empty);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException(INVALID_CUSTOMER_NUMBER.getMessage());
        }

        if (isEmpty()) {
            throw new IllegalArgumentException(EMPTY_ORDER_TABLE.getMessage());
        }

        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeEmpty(final boolean empty) {
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException(ALREADY_TABLE_GROUP.getMessage());
        }

        if (isNotCompletedOrders()) {
            throw new IllegalArgumentException(NOT_COMPLETED_ORDER.getMessage());
        }

        this.empty = empty;
    }

    private boolean isNotCompletedOrders() {
        return orders.stream().anyMatch(order -> isNotCompleted(order.getOrderStatus()));
    }

    private boolean isNotCompleted(final OrderStatus orderStatus) {
        return orderStatus.equals(OrderStatus.COOKING) || orderStatus.equals(OrderStatus.MEAL);
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public void group(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void addOrder(final Order order) {
        this.orders.add(order);
    }

    public void unGroup() {
        tableGroup = null;
    }
}
