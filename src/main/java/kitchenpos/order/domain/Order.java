package kitchenpos.order.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static kitchenpos.order.application.OrderCrudService.ORDERLINEITEMS_EMPTY_EXCEPTION_MESSAGE;


@Entity
@Table(name = "orders")
public class Order {
    public static final String ORDER_TABLE_NULL_EXCEPTION_MESSAGE = "주문 테이블이 없습니다.";
    public static final String COMPLETION_CHANGE_EXCEPTION_MESSAGE = "완료일 경우 변경할 수 없습니다.";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    public Order(Long orderTableId, List<OrderLineItem> orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException(ORDERLINEITEMS_EMPTY_EXCEPTION_MESSAGE);
        }
        if (Objects.isNull(orderTableId)) {
            throw new IllegalArgumentException(ORDER_TABLE_NULL_EXCEPTION_MESSAGE);
        }
        this.orderTableId = orderTableId;
        for (OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.setOrder(this);
        }
        this.orderLineItems.addAll(orderLineItems);
//        this.orderLineItems = orderLineItems;
        this.orderStatus = OrderStatus.COOKING.name();
        this.orderedTime = LocalDateTime.now();
    }

    public Order(long id, long orderTableId, String status, LocalDateTime orderedTime) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = status;
        this.orderedTime = orderedTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public void setOrderTableId(final Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void setOrderedTime(final LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
    }

    public void meal() {
        if (this.orderStatus.equals(OrderStatus.COMPLETION.name())) {
            throw new IllegalArgumentException(COMPLETION_CHANGE_EXCEPTION_MESSAGE);
        }
        this.orderStatus = OrderStatus.MEAL.name();
    }

    public void complete() {
        this.orderStatus = OrderStatus.COMPLETION.name();
    }

    public List<OrderLineItem> getOrderLineItems() {
        return this.orderLineItems.getOrderLineItems();
    }
}
