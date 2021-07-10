package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import kitchenpos.handler.exception.NotChangeStatusException;
import kitchenpos.table.domain.OrderTable;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_table_id")
    private Long orderTableId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @CreatedDate
    private LocalDateTime orderedTime;

    @Embedded
    private final OrderLineItems orderLineItems = new OrderLineItems();

    public Order() {
        this.orderStatus = OrderStatus.COOKING;
    }

    public Order(List<OrderLineItem> orderLineItems) {
        this.orderStatus = OrderStatus.COOKING;
        orderLineItems.forEach(this::addOrderLineItem);
    }

    public void addOrderLineItem(OrderLineItem item) {
        orderLineItems.add(item);
        item.relatedTo(this);
    }

    public void changeOrderStatus(String orderStatus) {
        changeOrderStatus(OrderStatus.valueOf(orderStatus));
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (this.orderStatus.isCompletedOrder()) {
            throw new NotChangeStatusException("완료된 주문의 상태는 변경할 수 없습니다.");
        }

        this.orderStatus = orderStatus;
    }

    public void group(OrderTable orderTable) {
        this.orderTableId = orderTable.getId();
    }

    public boolean hasNotCompletedOrder() {
        return orderStatus.isNotCompletedOrder();
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

    public OrderLineItems getOrderLineItems() {
        return orderLineItems;
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
        return Objects.equals(id, order.id)
            && Objects.equals(orderTableId, order.orderTableId)
            && orderStatus == order.orderStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderTableId, orderStatus);
    }
}
