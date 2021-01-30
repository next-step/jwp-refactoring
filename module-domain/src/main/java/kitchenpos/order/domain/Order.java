package kitchenpos.order.domain;

import kitchenpos.advice.exception.OrderException;
import kitchenpos.advice.exception.OrderLineItemException;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_table_id")
    private Long orderTableId;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @CreatedDate
    @Column(name = "ordered_time", updatable = false)
    private LocalDateTime orderedTime;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderLineItem> orderLineItems;

    protected Order() {
    }

    public static Order ofCooking(Long orderTableId) {
        return new Order(orderTableId, OrderStatus.COOKING);
    }

    public Order(Long orderTableId, OrderStatus orderStatus) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = LocalDateTime.now();
        this.orderLineItems = new ArrayList<>();
    }


    public void validateOrderStatus(OrderStatus orderStatus) {
        if (Objects.equals(this.orderStatus, orderStatus)) {
            throw new OrderException("주문상태가 올바르지 않습니다", orderStatus.name());
        }
    }

    public void validateMenuSize(long size) {
        if (orderLineItems.size() != size) {
            throw new OrderException("주문 메뉴의 사이즈가 다릅니다", size);
        }
    }

    public void addOrderLineItem(OrderLineItem orderLineItem) {
        orderLineItem.updateOrderId(this.id);
        orderLineItems.add(orderLineItem);
    }

    public void updateOrderStatus(OrderStatus orderStatus) {
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

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void validateEmptyOrderLineItems() {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new OrderLineItemException("orderLineItems가 존재하지 않습니다");
        }
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderTable id=" + orderTableId +
                ", orderStatus=" + orderStatus +
                ", orderedTime=" + orderedTime +
                ", orderLineItems size =" + orderLineItems.size() +
                '}';
    }
}
