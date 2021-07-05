package kitchenpos.domain;

import kitchenpos.table.domain.OrderTable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Table(name = "orders")
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "orderId")
    private List<OrderLineItem> orderLineItems;

    public Order() {
    }

    public Order(Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static Order newOrder(OrderTable orderTable, List<OrderLineItem> newOrderLineItems) {

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈테이블은 주문을 할수 없습니다.");
        }

        return new Order(orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), newOrderLineItems);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public void setOrderTableId(final Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void setOrderedTime(final LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void setOrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public void changeOrderStatus(String orderStatus) {
        if (Objects.equals(OrderStatus.COMPLETION.name(), getOrderStatus())) {
            throw new IllegalArgumentException("완료상태인 준문은 상태변경이 불가능합니다.");
        }

        setOrderStatus(orderStatus);
    }
}
