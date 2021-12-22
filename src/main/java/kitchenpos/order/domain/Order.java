package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.common.domain.BaseEntity;
import kitchenpos.exception.CannotUpdatedException;
import kitchenpos.exception.InvalidArgumentException;

@Entity(name = "orders")
public class Order extends BaseEntity {

    private static final Integer MIN_SIZE = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderTableId;

    private final LocalDateTime orderedTime = LocalDateTime.now();

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    @Embedded
    private final OrderLineItems orderLineItems = new OrderLineItems();

    protected Order() {
    }

    private Order(Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        initOrderStatus(orderStatus);
        addOrderLineItems(orderLineItems);
    }

    public static Order of(Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        return new Order(orderTableId, orderStatus, orderLineItems);
    }

    public void addOrderLineItems(List<OrderLineItem> orderLineItems) {
        validateAddOrderLineItem(orderLineItems);

        for (OrderLineItem orderLineItem : orderLineItems) {
            addOrderLineItem(orderLineItem);
        }
    }

    public boolean isOnGoing() {
        return OrderStatus.COOKING.equals(orderStatus) || OrderStatus.MEAL.equals(orderStatus);
    }

    public void removeOrderLineItem(OrderLineItem orderLineItem) {
        orderLineItems.remove(orderLineItem);
    }

    public void updateOrderStatus(OrderStatus orderStatus) {
        validateUpdateOrderStatus();
        this.orderStatus = orderStatus;

    }

    protected void addOrderLineItem(OrderLineItem orderLineItem) {
        orderLineItems.add(orderLineItem);

        if (!orderLineItem.equalsOrder(this)) {
            orderLineItem.relateOrder(this);
        }
    }

    private void initOrderStatus(OrderStatus orderStatus) {
        if (Objects.isNull(orderStatus)) {
            orderStatus = OrderStatus.COOKING;
        }
        this.orderStatus = orderStatus;
    }

    private void validateUpdateOrderStatus() {
        if (OrderStatus.COMPLETION.equals(orderStatus)) {
            throw new CannotUpdatedException("계산완료된 주문은 변경할 수 없습니다.");
        }
    }


    private void validateAddOrderLineItem(List<OrderLineItem> orderLineItems) {
        if (orderLineItems.size() < MIN_SIZE) {
            throw new InvalidArgumentException("메뉴는 하나 이상 선택해야 합니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatusName() {
        return orderStatus.name();
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.get();
    }

}
