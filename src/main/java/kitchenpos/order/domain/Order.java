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
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import kitchenpos.common.domain.RequireValidation;
import kitchenpos.common.exception.InvalidStatusException;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.Assert;

@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JoinColumn(nullable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_orders_order_table"))
    private long orderTableId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.COOKING;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems;

    protected Order() {
    }

    private Order(long orderTableId, OrderLineItems lineItems) {
        Assert.notNull(lineItems, "주문 항목들은 필수입니다.");
        Assert.isTrue(lineItems.isNotEmpty(), "주문 항목들이 비어있을 수 없습니다.");
        this.orderTableId = orderTableId;
        lineItems.changeOrder(this);
        this.orderLineItems = lineItems;
    }

    public static RequireValidation<Order> of(long orderTableId, List<OrderLineItem> lineItems) {
        return RequireValidation.from(new Order(orderTableId, OrderLineItems.from(lineItems)));
    }

    public long id() {
        return id;
    }

    public long tableId() {
        return orderTableId;
    }

    public OrderStatus status() {
        return orderStatus;
    }

    public LocalDateTime orderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> lineItems() {
        return orderLineItems.list();
    }

    public void changeStatus(OrderStatus status) {
        Assert.notNull(status, "변경하려는 상태는 필수입니다.");
        if (orderStatus.isCompleted()) {
            throw new InvalidStatusException(String.format("완료된 주문(%s)의 상태를 변경할 수 없습니다.", this));
        }
        this.orderStatus = status;
    }

    public boolean isCookingOrMeal() {
        return orderStatus.isCooking() || orderStatus.isMeal();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
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
        return id == order.id;
    }

    @Override
    public String toString() {
        return "Order{" +
            "id=" + id +
            ", orderTableId=" + orderTableId +
            ", orderStatus=" + orderStatus +
            ", orderedTime=" + orderedTime +
            '}';
    }
}
