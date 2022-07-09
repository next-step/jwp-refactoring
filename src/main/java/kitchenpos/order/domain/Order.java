package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.util.ObjectUtils;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderTableId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems;

    protected Order() {

    }

    public Order(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime,
                 OrderLineItems orderLineItems) {
        this(orderTableId, orderStatus, orderedTime, orderLineItems);
        this.id = id;
    }

    public Order(Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime,
                 OrderLineItems orderLineItems) {
        validateOrderTableId(orderTableId);
        validateOrderLineItems(orderLineItems);
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    private void validateOrderTableId(Long orderTableId) {
        if (ObjectUtils.isEmpty(orderTableId)) {
            throw new IllegalArgumentException("주문 테이블은 존재 해야합니다");
        }
    }

    private void validateOrderLineItems(OrderLineItems orderLineItems) {
        if (ObjectUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문 항목들은 존재 해야합니다.");
        }
    }

    public static Order createOrder(Long orderTableId, OrderLineItems orderLineItems) {
        Order order = new Order(orderTableId, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
        orderLineItems.changeOrder(order);
        return order;
    }

    public void changOrderStatus(OrderStatus orderStatus) {
        if (ObjectUtils.isEmpty(orderStatus)) {
            throw new IllegalArgumentException("변경할 상태값이 없습니다.");
        }
        if (this.orderStatus.equals(OrderStatus.COMPLETION)) {
            throw new IllegalArgumentException("주문이 완료되어 상태를 변경할 수 없다.");
        }
        this.orderStatus = orderStatus;
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
        return orderLineItems.value();
    }

    public Long getId() {
        return id;
    }

}
