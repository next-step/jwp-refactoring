package kitchenpos.order.domain;

import kitchenpos.BaseEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    private static final String ERR_TEXT_INVALID_ORDER_TABLE_ID = "유효하지 않은 주문 테이블 아이디 입니다.";
    private static final int IS_LESS_THAN_ZERO = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderTableId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Embedded
    private final OrderLineItems orderLineItems = OrderLineItems.createInstance();

    protected Order() {
    }

    protected Order(final Long orderTableId) {
        this.orderTableId = orderTableId;
        this.orderStatus = OrderStatus.COOKING;
    }

    public static Order of(final Long orderTableId) {
        if (orderTableId == null || orderTableId.compareTo(0L) <= IS_LESS_THAN_ZERO) {
            throw new IllegalArgumentException(ERR_TEXT_INVALID_ORDER_TABLE_ID);
        }

        return new Order(orderTableId);
    }

    public void changeOrderStatus(final OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void addOrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems.add(orderLineItems);
        this.orderLineItems.linkOrderId(this.getId());
    }

    public boolean isOrderStatusEqualsCompletion() {
        return this.orderStatus.isCompletion();
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
        return this.orderLineItems.orderLineItems();
    }
}
