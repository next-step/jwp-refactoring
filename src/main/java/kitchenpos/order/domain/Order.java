package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(value = AuditingEntityListener.class)
@Table(name = "orders")
public class Order {
    private static final String REQUIRED_TABLE = "주문 테이블은 필수 값 입니다.";
    private static final String REQUIRED_ORDER_LINE_ITEM = "주문 항목은 비어있을 수 없습니다.";
    private static final String ORDER_COMPLETED = "완료된 주문의 상태를 변경할 수 없습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderTableId;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    @CreatedDate
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    protected Order() {}

    public Order(Long orderTableId) {
        validate(orderTableId);
        this.orderTableId = orderTableId;
        this.orderStatus = OrderStatus.COOKING;
    }

    private void validate(Long orderTableId) {
        if (Objects.isNull(orderTableId)) {
            throw new IllegalArgumentException(REQUIRED_TABLE);
        }
    }

    public void addLineItem(OrderLineItem orderLineItem) {
        orderLineItems.add(orderLineItem);
        orderLineItem.addOrder(this);
    }

    public void checkOrderLineItems() {
        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException(REQUIRED_ORDER_LINE_ITEM);
        }
    }

    public void changeStatus(OrderStatus orderStatus) {
        if (this.orderStatus.equals(OrderStatus.COMPLETION)) {
            throw new IllegalArgumentException(ORDER_COMPLETED);
        }

        this.orderStatus = orderStatus;
    }

    public List<Long> findMenus() {
        return Collections.unmodifiableList(this.orderLineItems.assignedMenu());
    }

    public Long getId() {
        return id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return this.orderLineItems.value();
    }
}
