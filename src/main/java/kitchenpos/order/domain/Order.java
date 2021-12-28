package kitchenpos.order.domain;

import com.google.common.collect.Lists;
import kitchenpos.menu.domain.Menu;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderTableId;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @CreatedDate
    private LocalDateTime orderedTime;
    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    public Order() {
    }

    public Order(Long orderTableId, OrderStatus orderStatus) {
        this(null, orderTableId, orderStatus);
    }

    public Order(Long id, Long orderTableId, OrderStatus orderStatus) {
        this(id, orderTableId, orderStatus, Lists.newArrayList());
    }

    public Order(Long id, Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = new OrderLineItems(orderLineItems);
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

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public OrderLineItems getOrderLineItems() {
        return orderLineItems;
    }

    public void changeOrderStatus(String orderStatus) {
        this.orderStatus = OrderStatus.valueOf(orderStatus);
    }

    public void addOrderLineItem(Long menu, long quantity) {
        this.orderLineItems.add(this, menu, quantity);
    }
}
