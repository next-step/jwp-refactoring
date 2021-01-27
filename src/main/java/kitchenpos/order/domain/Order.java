package kitchenpos.order.domain;

import kitchenpos.advice.exception.OrderException;
import kitchenpos.advice.exception.OrderLineItemException;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id", nullable = false)
    private OrderTable orderTable;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @CreatedDate
    @Column(name = "ordered_time", updatable = false)
    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order")
    private List<OrderLineItem> orderLineItems;

    protected Order() {
    }

    public static Order ofCooking(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        return new Order(orderTable, OrderStatus.COOKING, orderLineItems);
    }

    private Order(OrderTable orderTable, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = LocalDateTime.now();
        this.orderLineItems = orderLineItems;
        validateEmptyOrderLineItems();
    }

    public void validateOrderStatus(OrderStatus orderStatus) {
        if (Objects.equals(this.orderStatus, orderStatus)) {
            throw new OrderException("주문상태가 올바르지 않습니다", orderStatus);
        }
    }

    public void validateMenuSize(long size) {
        if (orderLineItems.size() != size) {
            throw new OrderException("주문 메뉴의 사이즈가 다릅니다", size);
        }
    }


    public void updateOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Long getId() {
        return id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    private void validateEmptyOrderLineItems() {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new OrderLineItemException("orderLineItems가 존재하지 않습니다");
        }
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderTable id=" + orderTable.getId() +
                ", orderStatus=" + orderStatus +
                ", orderedTime=" + orderedTime +
                ", orderLineItems size =" + orderLineItems.size() +
                '}';
    }
}
