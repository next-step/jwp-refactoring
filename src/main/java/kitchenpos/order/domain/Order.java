package kitchenpos.order.domain;

import kitchenpos.BaseOrderEntity;
import kitchenpos.order.dto.OrderLineItemResponse;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity(name = "orders")
public class Order extends BaseOrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_table_id", nullable = false)
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    protected Order() {
    }

    public Order(OrderTable orderTable, OrderStatus orderStatus) {
        validateOrderTableIsEmpty(orderTable);
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
    }

    private void validateOrderTableIsEmpty(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 비어있는 경우 주문할 수 없습니다.");
        }
    }

    public void add(List<OrderLineItem> orderLineItems) {
        this.orderLineItems.add(this.id, orderLineItems);
    }


    public void changeOrderStatus(OrderStatus orderStatus) {
        validateOrderStatusAlreadyCompletion();
        this.orderStatus = orderStatus;
    }

    private void validateOrderStatusAlreadyCompletion() {
        if (Objects.equals(this.orderStatus.name(), OrderStatus.COMPLETION.name())) {
            throw new IllegalArgumentException("이미 완료된 상태는 변경할 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.getOrderLineItems();
    }

    public Long getOrderTableId() {
        return orderTable.getId();
    }

    public List<OrderLineItemResponse> getOrderLineItemResponses() {
        return orderLineItems.getOrderLineItemResponse();
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }
}
