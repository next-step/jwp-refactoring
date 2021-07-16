package kitchenpos.domain.orders.order.domain;

import kitchenpos.domain.orders.orderTable.event.OrderTableCreatedEvent;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order extends AbstractAggregateRoot<Order> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_table_id")
    private Long orderTableId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems(this);

    @CreatedDate
    private LocalDateTime orderedTime;

    protected Order() {
    }

    public Order(final Long id, final Long orderTableId, final OrderLineItem... orderLineItems) {
        validate(orderTableId);

        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = OrderStatus.COOKING;
        this.orderLineItems.add(Arrays.asList(orderLineItems));
    }

    private void validate(final Long orderTableId) {
        registerEvent(new OrderTableCreatedEvent(orderTableId));
    }

    public Order(final Long orderTableId, final OrderLineItem... orderLineItems) {
        this(null, orderTableId, orderLineItems);
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

    public boolean isCompletionStatus() {
        return Objects.equals(orderStatus, OrderStatus.COMPLETION);
    }

    public boolean isNotCompletionStatus() {
        return !isCompletionStatus();
    }

    public List<Long> getOrderLineItemIds() {
        return orderLineItems.list().stream()
            .map(orderLineItem -> orderLineItem.getMenuId())
            .collect(Collectors.toList());
    }

    public boolean equalsByOrderStatus(OrderStatus orderStatus) {
        return this.orderStatus == orderStatus;
    }

    public void changeOrderStatus(final OrderStatus orderStatus) {
        if (isCompletionStatus()) {
            throw new IllegalArgumentException(String.format("%s의 상태는 변경 불가능합니다.", OrderStatus.COMPLETION.remark()));
        }

        this.orderStatus = orderStatus;
    }

    public boolean isSameSize(final Long size) {
        return orderLineItems.isSameSize(size);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Order that = (Order) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public enum OrderStatus {
        COOKING("조리"), MEAL("식사"), COMPLETION("계산 완료");

        private final String remark;

        OrderStatus(final String remark) {
            this.remark = remark;
        }

        public String remark() {
            return remark;
        }
    }
}
