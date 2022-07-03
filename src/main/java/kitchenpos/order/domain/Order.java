package kitchenpos.order.domain;

import kitchenpos.order.exception.IllegalOrderException;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "order_table_id", nullable = false)
    private Long orderTableId;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.COOKING;
    @CreatedDate
    private LocalDateTime orderedTime;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    public static final String ERROR_ORDER_INVALID_STATUS = "주문의 상태는 %s일 수 없습니다.";

    protected Order() {
    }

    private Order(Long id, Long orderTableId, List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        registerOrderLineItems(orderLineItems);
    }

    private Order(Long orderTableId, List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        registerOrderLineItems(orderLineItems);
    }

    public static Order of(Long orderTableId, List<OrderLineItem> orderLineItems) {
        return new Order(orderTableId, orderLineItems);
    }

    public static Order of(Long id, Long orderTableId, List<OrderLineItem> orderLineItems) {
        return new Order(id, orderTableId, orderLineItems);
    }

    private void registerOrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
        orderLineItems.forEach(orderLineItem -> orderLineItem.registerOrder(this));
    }

    public void changeStatus(OrderStatus orderStatus) {
        validateOrderStatusChangeable();
        this.orderStatus = orderStatus;
    }

    private void validateOrderStatusChangeable() {
        if (OrderStatus.COMPLETION.equals(this.orderStatus)) {
            throw new IllegalOrderException(
                    String.format(ERROR_ORDER_INVALID_STATUS, OrderStatus.COMPLETION)
            );
        }
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
