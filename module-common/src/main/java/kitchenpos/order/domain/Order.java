package kitchenpos.order.domain;

import java.time.*;
import java.util.*;

import javax.persistence.*;
import javax.persistence.Id;

import org.springframework.data.annotation.*;

import kitchenpos.common.*;
import kitchenpos.menu.domain.*;
import kitchenpos.table.domain.*;

@Entity
public class Order {
    private static final String ORDER_TABLE = "주문테이블";
    private static final String CANNOT_CHANGE_ORDER_STATUS_EXCEPTION_STATEMENT = "주문이 완료되어 주문상태를 변경할 수 없습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Embedded
    private final OrderLineItems orderLineItems = new OrderLineItems();

    @CreatedDate
    private final LocalDateTime orderedTime = LocalDateTime.now();

    protected Order() {
    }

    public Order(OrderTable orderTable) {
        validate(orderTable);
        this.orderTable = orderTable;
        orderStatus = OrderStatus.COOKING;
    }

    public static Order from(OrderTable orderTable) {
        return new Order(orderTable);
    }

    private void validate(OrderTable orderTable) {
        if (Objects.isNull(orderTable)) {
            throw new WrongValueException(ORDER_TABLE);
        }
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (this.orderStatus.equals(OrderStatus.COMPLETION)) {
            throw new IllegalArgumentException(CANNOT_CHANGE_ORDER_STATUS_EXCEPTION_STATEMENT);
        }
        this.orderStatus = orderStatus;
    }

    public Long getId() {
        return id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public OrderLineItems getOrderLineItems() {
        return orderLineItems;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void addOrderLineItem(Menu menu, int quantity) {
        orderLineItems.add(this.id, menu, quantity);
    }
}
