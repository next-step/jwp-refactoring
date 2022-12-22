package kitchenpos.order.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderTableId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    protected Order() {
    }

    public Order(Long orderTableId) {
        validateOrderTable(orderTableId);
        changeOrderStatus(OrderStatus.COOKING);
        this.orderTableId = orderTableId;
    }

    private void validateOrderTable(Long orderTableId) {
        if (Objects.isNull(orderTableId)) {
            throw new IllegalArgumentException("빈 테이블에서는 주문을 할 수 없습니다");
        }
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        validateOrderStatus();
        this.orderStatus = orderStatus;
    }

    private void validateOrderStatus() {
        if (this.orderStatus == null) {
            return;
        }
        if (this.orderStatus.equals(OrderStatus.COMPLETION)) {
            throw new IllegalArgumentException("이미 주문이 완료되었습니다.");
        }
    }

    public void order(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문 항목이 비어있을 수 없습니다.");
        }
        orderLineItems.forEach(this::addOrderLineItem);
    }

    void addOrderLineItem(OrderLineItem orderLineItem) {
        this.orderLineItems.addOrderLineItem(this, orderLineItem);
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
