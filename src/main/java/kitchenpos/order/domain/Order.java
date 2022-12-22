package kitchenpos.order.domain;

import kitchenpos.common.BaseEntity;
import kitchenpos.common.ErrorCode;
import kitchenpos.tablegroup.domain.OrderTable;
import org.springframework.util.CollectionUtils;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private static OrderStatus orderStatus;

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    protected Order() {}

    public Order(OrderTable orderTable, OrderStatus orderStatus) {
        validate(orderTable);

        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
    }

    private void validate(OrderTable orderTable) {
        if(Objects.isNull(orderTable)) {
            throw new IllegalArgumentException(ErrorCode.INVALID_FORMAT_ORDER.getErrorMessage());
        }

        if(orderTable.isEmpty()) {
            throw new IllegalArgumentException(ErrorCode.ORDER_TABLES_CANNOT_BE_EMPTY.getErrorMessage());
        }
    }

    public void order(List<OrderLineItem> orderLineItems) {
        if(CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException(ErrorCode.ORDER_LINE_ITEMS_CANNOT_BE_EMPTY.getErrorMessage());
        }

        orderLineItems.forEach(orderLineItem -> addOrderLineItem(orderLineItem));
    }

    void addOrderLineItem(OrderLineItem orderLineItem) {
        this.orderLineItems.addOrderLineItem(this, orderLineItem);
    }

    public void checkForChangingOrderTable() {
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

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.getOrderLineItems();
    }

    public static void checkCookingOrEatingMealOrder(Order order) {
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
