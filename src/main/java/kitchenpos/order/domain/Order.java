package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_table_id")
    private Long orderTableId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems;

    protected Order() {
    }

    public Order(Long orderTableId, List<OrderLineItem> orderLineItemsParam) {
        this.orderTableId = orderTableId;
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = LocalDateTime.now();
        this.orderLineItems = new OrderLineItems();
        addLineItems(orderLineItemsParam);
    }

    public void addLineItems(List<OrderLineItem> orderLineItemsParam) {
        orderLineItemsParam.forEach(orderLineItem -> orderLineItem.changeOrder(this));
        orderLineItems.addOrderLineItems(orderLineItemsParam);
    }

    public void changeStatus(String orderStatusParam) {
        validateOrderStatus();
        this.orderStatus = OrderStatus.valueOf(orderStatusParam);
    }

    private void validateOrderStatus() {
        if (Objects.equals(OrderStatus.COMPLETION.name(), getOrderStatus())) {
            throw new IllegalArgumentException("완료 상태의 주문은 상태를 변경할 수 없습니다.");
        }
    }

    public boolean isSameStatus(List<String> orderStatuses) {
        return orderStatuses.contains(this.orderStatus.name());
    }

    public List<Long> makeMenuIds() {
        return orderLineItems.makeMenuIds();
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus.name();
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.getOrderLineItems();
    }
}
