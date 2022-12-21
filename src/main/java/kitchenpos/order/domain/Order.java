package kitchenpos.order.domain;

import kitchenpos.order.domain.type.OrderStatus;
import kitchenpos.menu.domain.Menu;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.common.constants.ErrorCodeType.*;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderTableId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @CreatedDate
    private LocalDateTime orderedTime = LocalDateTime.now();

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    protected Order() {
    }

    public Order(Long orderTableId, OrderStatus orderStatus) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
    }

    public Order(Long orderTableId, OrderStatus orderStatus, OrderLineItems orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = LocalDateTime.now();
        this.orderLineItems = orderLineItems;
    }

    public Order(Long id, Long orderTableId, OrderStatus orderStatus, OrderLineItems orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = LocalDateTime.now();;
        this.orderLineItems = orderLineItems;
    }

    public void validUngroupable() {
        if (Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL).contains(this.orderStatus)) {
            throw new IllegalArgumentException(COOKING_MEAL_NOT_UNGROUP.getMessage());
        }
    }

    public void addOrderLineItems(List<OrderLineItem> orderLineItem, List<Menu> menu) {
        this.orderLineItems = new OrderLineItems(orderLineItem);
        orderLineItems.setOrderLineItem(this);
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

    public void changeOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
