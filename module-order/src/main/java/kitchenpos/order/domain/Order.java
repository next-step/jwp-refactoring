package kitchenpos.order.domain;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long orderTableId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @CreatedDate
    private LocalDateTime orderedTime;

    @Embedded
    private final OrderLineItems orderLineItems = new OrderLineItems();

    protected Order() {
    }

    private Order(Long id, Long orderTableId, OrderStatus orderStatus, OrderLineItems orderLineItems) {
        this(orderTableId, orderLineItems);
        this.id = id;
        this.orderStatus = orderStatus;
    }

    private Order(Long orderTableId, OrderLineItems orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = LocalDateTime.now();
        addOrderLineItems(orderLineItems);
    }

    public static Order of(Long orderTableId, OrderLineItems orderLineItems) {
        return new Order(orderTableId, orderLineItems);
    }

    public static Order of(Long id, Long orderTableId, OrderStatus orderStatus, OrderLineItems orderLineItems) {
        return new Order(id, orderTableId, orderStatus, orderLineItems);
    }

    private void addOrderLineItems(OrderLineItems orderLineItems) {
        orderLineItems.getOrderLineItems().forEach(orderLineItem -> {
            orderLineItem.setOrder(this);
            this.orderLineItems.add(orderLineItem);
        });
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
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
        return orderLineItems.getOrderLineItems();
    }
}
