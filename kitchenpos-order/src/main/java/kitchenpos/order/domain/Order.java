package kitchenpos.order.domain;

import kitchenpos.orderstatus.domain.Status;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_table_id")
    private Long orderTableId;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "order_status")
    private Status orderStatus;
    @Column(name = "ordered_time")
    private LocalDateTime orderedTime;
    @Embedded
    private OrderLineItemBag orderLineItemBag;

    private Order(Long orderTableId, Status orderStatus, LocalDateTime orderedTime,
            OrderLineItemBag orderLineItemBag) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItemBag = orderLineItemBag;
    }

    public static Order of(Long orderTableId, Status orderStatus, LocalDateTime orderedTime,
            OrderLineItemBag orderLineItemBag) {
        return new Order(orderTableId, orderStatus, orderedTime, orderLineItemBag);
    }

    protected Order() {
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public Status getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public OrderLineItemBag getOrderLineItemBag() {
        return orderLineItemBag;
    }

    public boolean isStatus(Status status) {
        return this.orderStatus.equals(status);
    }

    public void changeStatus(Status orderStatus) {
        validChangeableStatus();
        this.orderStatus = orderStatus;
    }

    private void validChangeableStatus() {
        if (this.orderStatus.equals(Status.COMPLETION)) {
            throw new IllegalArgumentException("계산 완료 된 주문은 주문 상태를 변경할 수 없습니다");
        }
    }

    public List<Long> menuIds() {
        return this.orderLineItemBag.menuIds();
    }

    public void updateItemOrder() {
        this.orderLineItemBag.updateItemOrder(this);
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
                order.orderStatus) && Objects.equals(orderLineItemBag, order.orderLineItemBag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderTableId, orderStatus, orderLineItemBag);
    }
}
