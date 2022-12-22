package kitchenpos.order.domain;

import kitchenpos.common.BaseEntity;
import kitchenpos.common.ErrorCode;
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

    public Order(Long orderTableId, OrderStatus orderStatus) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
    }

    public void order(List<OrderLineItem> orderLineItems) {
        orderLineItems.forEach(this::addOrderLineItem);
    }

    void addOrderLineItem(OrderLineItem orderLineItem) {
        this.orderLineItems.addOrderLineItem(this, orderLineItem);
    }

    public void checkOrderStatus() {
        if(orderStatus.isCooking() || orderStatus.isMeal()) {
            throw new IllegalArgumentException(ErrorCode.CANNOT_BE_CHANGED_ORDER_STATUS.getErrorMessage());
        }
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (this.orderStatus.isCompletion()) {
            throw new IllegalArgumentException(ErrorCode.CANNOT_BE_CHANGED_ORDER_STATUS.getErrorMessage());
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
            throw new IllegalArgumentException(ErrorCode.CANNOT_BE_CHANGED_ORDER_STATUS.getErrorMessage());
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

        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
