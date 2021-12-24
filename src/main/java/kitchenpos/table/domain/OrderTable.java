package kitchenpos.table.domain;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import java.util.List;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.common.exception.Message;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.Orders;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Enumerated(EnumType.STRING)
    private OrderTableStatus orderTableStatus = OrderTableStatus.EMPTY;

    @Embedded
    private Orders orders = new Orders();

    public static OrderTable of(NumberOfGuests numberOfGuests, OrderTableStatus orderTableStatus) {
        return new OrderTable(null, numberOfGuests, orderTableStatus);
    }

    public static OrderTable of(Long id, NumberOfGuests numberOfGuests,
        OrderTableStatus orderTableStatus, List<Order> orders) {
        return new OrderTable(id, numberOfGuests, orderTableStatus, orders);
    }

    private OrderTable(Long id, NumberOfGuests numberOfGuests, OrderTableStatus orderTableStatus) {
        validOrderTableStatusIsNotNull(orderTableStatus);
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.orderTableStatus = orderTableStatus;
    }

    public OrderTable(Long id, NumberOfGuests numberOfGuests, OrderTableStatus orderTableStatus,
        List<Order> orders) {
        this(id, numberOfGuests, orderTableStatus);
        this.orders = new Orders(orders);
    }

    protected OrderTable() {
    }

    private void validOrderTableStatusIsNotNull(OrderTableStatus orderTableStatus) {
        if (Objects.isNull(orderTableStatus)) {
            throw new IllegalArgumentException(
                Message.ORDER_TABLE_IS_NOT_ORDER_TABLE_STATUS_NULL.getMessage());
        }
    }

    public void changeOrderTableStatus(boolean empty) {
        validCookieOreMeal();
        this.orderTableStatus = OrderTableStatus.valueOf(empty);
    }

    public void changeNumberOfGuest(int numberOfGuest) {
        this.numberOfGuests = new NumberOfGuests(numberOfGuest);
    }

    public void unGroup() {
        validCookieOreMeal();
        tableGroup = null;
    }

    private void validCookieOreMeal() {
        orders.validCookieOrMeal();
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public NumberOfGuests getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return orderTableStatus.isEmpty();
    }

    public void withTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public Orders getOrders() {
        return orders;
    }

}
