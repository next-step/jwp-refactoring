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


    public Order() {
    }

    public Order(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        this.orderTable = orderTable;
        this.orderLineItems = OrderLineItems.of(orderLineItems);
        this.orderLineItems.mapOrder(this);
        this.orderStatus = OrderStatus.COOKING;
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

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getOrderTableId() {
        return orderTable.getId();
    }

    @Deprecated
    public void setOrderTableId(final Long orderTableId) {
        this.orderTable.setId(orderTableId);
    }

    public String getOrderStatus() {
        return orderStatus.name();
    }


    @Deprecated
    public void setOrderStatus(final String orderStatus) {
        this.orderStatus = OrderStatus.valueOf(orderStatus);
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void setOrderedTime(final LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.getOrderLineItems();
    }

    public void setOrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = OrderLineItems.of(orderLineItems);
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
