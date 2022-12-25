package kitchenpos.order.domain;

import kitchenpos.common.BaseEntity;
import kitchenpos.order.exception.OrderExceptionConstants;
import kitchenpos.tablegroup.domain.OrderTable;
import org.springframework.util.CollectionUtils;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderTableId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    protected Order() {}

    private Order(Long orderTableId) {
        this.orderTableId = orderTableId;
        this.orderStatus = OrderStatus.COOKING;
    }

    private Order(Long orderTableId, List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = OrderLineItems.of(orderLineItems);
        this.orderStatus = OrderStatus.COOKING;
    }

    public static Order from(Long orderTableId) {
        return new Order(orderTableId);
    }

    void addOrderLineItem(OrderLineItem orderLineItem) {
        this.orderLineItems.addOrderLineItem(this, orderLineItem);
    }

    public void checkOrderStatus() {
        if(orderStatus.isCooking() || orderStatus.isMeal()) {
            throw new IllegalArgumentException(OrderExceptionConstants.CANNOT_BE_CHANGED_ORDER_STATUS.getErrorMessage());
        }
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (this.orderStatus.isCompletion()) {
            throw new IllegalArgumentException(OrderExceptionConstants.CANNOT_BE_CHANGED_ORDER_STATUS.getErrorMessage());
        }

        this.orderStatus = orderStatus;
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

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.getOrderLineItems();
    }

    public void checkCookingOrEatingMealOrder() {
        if(orderStatus.isCooking() || orderStatus.isMeal()) {
            throw new IllegalArgumentException(OrderExceptionConstants.CANNOT_BE_CHANGED_ORDER_STATUS.getErrorMessage());
        }
    }

    public void addOrderLineItems(List<OrderLineItem> orderLineItems) {
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

        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
