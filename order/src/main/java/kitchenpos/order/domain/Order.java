package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.domain.Validator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderTableId;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Embedded
    private OrderLineItems orderLineItems;

    @CreatedDate
    private LocalDateTime orderedDateTime;

    protected Order() {
    }

    public Order(
        final Long id,
        final Long orderTableId,
        final OrderLineItems orderLineItems,
        final Validator<Order> validator
    ) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
        this.status = OrderStatus.COOKING;

        validator.validate(this);
    }

    public Order(
        final Long orderTableId,
        final OrderLineItems orderLineItems,
        final Validator<Order> validator
    ) {
        this(null, orderTableId, orderLineItems, validator);
    }

    public void changeStatus(final OrderStatus status) {
        if (this.status == OrderStatus.COMPLETION) {
            throw new IllegalStateException("이미 완료된 주문의 상태를 변경할 수 없습니다.");
        }
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.asList();
    }

    public LocalDateTime getOrderedDateTime() {
        return orderedDateTime;
    }

    public List<Long> getMenuIds() {
        return orderLineItems.getMenuIds();
    }
}
