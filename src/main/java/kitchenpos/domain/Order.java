package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Order {
    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItem> orderLineItems;

    public Order(Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public Order(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime,
            List<OrderLineItem> orderLineItems) {
        this(orderTableId, orderStatus, orderedTime, orderLineItems);
        this.id = id;
    }

    public Order() {
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

    public boolean isStatus(OrderStatus status) {
        return this.orderStatus.equals(status.name());
    }

    public Order changeStatus(OrderStatus orderStatus) {
        validChangableStatus(this.orderStatus);
        return new Order(this.id,
                this.orderTableId,
                orderStatus.name(),
                this.orderedTime,
                this.orderLineItems);
    }

    private void validChangableStatus(String orderStatus) {
        if (orderStatus.equals(OrderStatus.COMPLETION.name())) {
            throw new IllegalArgumentException("계산 완료 된 주문은 주문 상태를 변경할 수 없습니다");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Order order = (Order) o;
        return Objects.equals(orderTableId, order.orderTableId) && Objects.equals(orderStatus,
                order.orderStatus) && Objects.equals(orderLineItems, order.orderLineItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderTableId, orderStatus, orderLineItems);
    }
}
