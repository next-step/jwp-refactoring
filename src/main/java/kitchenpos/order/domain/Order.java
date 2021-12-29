package kitchenpos.order.domain;

import kitchenpos.common.domain.BaseEntity;
import kitchenpos.common.exception.IllegalArgumentException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "order_table_id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"), nullable = false)
    private Long orderTableId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems;

    protected Order() {}

    private Order(Long id, Long orderTableId, OrderLineItems orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = LocalDateTime.now();
        this.orderLineItems = orderLineItems;
    }

    public static Order of(Long orderTableId, List<OrderLineItem> orderLineItems) {
        return new Order(null, orderTableId, OrderLineItems.of(orderLineItems));
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        validateIsChangeable();
        this.orderStatus = orderStatus;
    }

    private void validateIsChangeable() {
        if (isComplete()) {
            throw new IllegalArgumentException("완료된 주문의 상태는 변경할 수 없습니다.");
        }
    }

    public boolean isComplete() {
        return orderStatus.isComplete();
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
        return orderLineItems.asList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
