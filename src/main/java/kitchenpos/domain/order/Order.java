package kitchenpos.domain.order;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_table_id", foreignKey = @ForeignKey(name = "fk_orders_order_table"), nullable = false)
    private OrderTable orderTable;

    @Embedded
    private OrderLineItems orderLineItems;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    protected Order() {
    }

    private Order(OrderTable orderTable, OrderLineItems orderLineItems, OrderStatus orderStatus,
        LocalDateTime orderedTime) {
        validOrderTableNotEmpty(orderTable);
        this.orderTable = orderTable;
        this.orderLineItems = orderLineItems;
        this.orderLineItems.mapOrder(this);
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public static Order of(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        return new Order(orderTable, OrderLineItems.of(orderLineItems), OrderStatus.COOKING,
            LocalDateTime.now());
    }

    public boolean isMatchOrderTable(OrderTable orderTable) {
        return this.orderTable.equals(orderTable);
    }

    public boolean isComplete() {
        return orderStatus.isComplete();
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (isComplete()) {
            throw new InvalidParameterException("결제완료 상태에서는 주문 상태를 변경 할 수 없습니다.");
        }

        this.orderStatus = orderStatus;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTable.getId();
    }

    public String getOrderStatus() {
        return orderStatus.name();
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.getOrderLineItems();
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    private void validOrderTableNotEmpty(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new InvalidParameterException("주문할 테이블은 빈테이블일 수 없습니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (Objects.isNull(id)) {
            return false;
        }

        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
