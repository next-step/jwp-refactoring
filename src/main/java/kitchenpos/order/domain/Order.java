package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
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
import kitchenpos.common.constant.ErrorCode;
import kitchenpos.ordertable.domain.OrderTable;

@Table(name = "ORDERS")
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "order_table_id", foreignKey = @ForeignKey(name = "fk_order_to_order_table"), nullable = false)
    private OrderTable orderTable;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @Column(nullable = false)
    private LocalDateTime orderedTime;
    @Embedded
    private OrderLineItems orderLineItems;

    protected Order() {}

    private Order(Long id, OrderTable orderTable, OrderLineItems orderLineItems) {
        validateOrderTable(orderTable);
        orderLineItems.setUpOrder(this);
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = LocalDateTime.now();
        this.orderLineItems = orderLineItems;
    }

    public static Order of(Long id, OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        return new Order(id, orderTable, OrderLineItems.from(orderLineItems));
    }

    public static Order of(OrderTable orderTable, OrderLineItems orderLineItems) {
        return new Order(null, orderTable, orderLineItems);
    }

    private void validateOrderTable(OrderTable orderTable) {
        if(orderTable.isEmpty()) {
            throw new IllegalArgumentException(ErrorCode.주문_테이블은_비어있으면_안됨.getErrorMessage());
        }
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        validateOrderStatusCompletion();
        this.orderStatus = orderStatus;
    }

    private void validateOrderStatusCompletion() {
        if(Objects.equals(this.orderStatus, OrderStatus.COMPLETION)) {
            throw new IllegalArgumentException(ErrorCode.이미_완료된_주문.getErrorMessage());
        }
    }

    public void validateIfNotCompletionOrder() {
        if(!Objects.equals(this.orderStatus, OrderStatus.COMPLETION)) {
            throw new IllegalArgumentException(ErrorCode.완료되지_않은_주문.getErrorMessage());
        }
    }

    public Long getId() {
        return id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public OrderLineItems getOrderLineItems() {
        return orderLineItems;
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
        return Objects.equals(getId(), order.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
