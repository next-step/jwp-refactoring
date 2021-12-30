package kitchenpos.moduledomain.table;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.moduledomain.common.exception.DomainMessage;
import kitchenpos.moduledomain.order.Order;
import kitchenpos.moduledomain.order.Orders;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
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
                DomainMessage.ORDER_TABLE_IS_NOT_ORDER_TABLE_STATUS_NULL.getMessage());
        }
    }

    public void changeOrderTableStatus(boolean empty) {
        validCookieOreMeal();
        this.orderTableStatus = OrderTableStatus.valueOf(empty);
    }

    public void changeNumberOfGuest(int numberOfGuest) {
        this.numberOfGuests = new NumberOfGuests(numberOfGuest);
    }

    public void group(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public void unGroup() {
        validCookieOreMeal();
        this.tableGroup = null;
    }

    private void validCookieOreMeal() {
        orders.validCookieOrMeal();
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        if (tableGroup == null) {
            return null;
        }
        return tableGroup.getId();
    }

    public NumberOfGuests getNumberOfGuests() {
        return numberOfGuests;
    }

    public OrderTableStatus getOrderTableStatus() {
        return orderTableStatus;
    }

    public boolean isEmpty() {
        return orderTableStatus.isEmpty();
    }

    public Orders getOrders() {
        return orders;
    }

}
