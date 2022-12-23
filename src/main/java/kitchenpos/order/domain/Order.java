package kitchenpos.order.domain;

import kitchenpos.order.constant.OrderStatus;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.domain.OrderTable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
public class Order {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private OrderTable orderTable;

    private String orderStatus;
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    protected Order() {}

    public Order(OrderTable orderTable, String orderStatus, LocalDateTime orderedTime) {
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public Order(OrderTable orderTable, String orderStatus, LocalDateTime orderedTime, OrderLineItems orderLineItems) {
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static Order create(OrderTable orderTable, String orderStatus,
                               LocalDateTime orderedTime, OrderLineItems orderLineItems,
                               OrderRequest orderRequest, long menuCount) {
        OrderValidator.validate(orderTable, orderRequest, menuCount);
        return new Order(orderTable, orderStatus, orderedTime, orderLineItems);
    }

    public void changeStatus(String orderStatus) {
        if (Objects.equals(OrderStatus.COMPLETION.name(), this.orderStatus)) {
            throw new IllegalArgumentException("완료된 주문은 변경할 수 없습니다.");
        }
        this.orderStatus = OrderStatus.valueOf(orderStatus).name();
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTable.getId();
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.value();
    }

}
