package kitchenpos.domain.order;

import kitchenpos.domain.order.exceptions.InvalidTryOrderException;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    public Order() {
    }

    Order(final Long id, final Long orderTableId, final String orderStatus, final LocalDateTime orderedTime, final List<OrderLineItem> orderLineItems) {
        validate(orderLineItems);
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static Order of(final Long id, final Long orderTableId, final String orderStatus, final LocalDateTime orderedTime) {
        return new Order(id, orderTableId, orderStatus, orderedTime, new ArrayList<>());
    }

    public Order(final Long orderTableId, final String orderStatus, final LocalDateTime orderedTime) {
        this(null, orderTableId, orderStatus, orderedTime, new ArrayList<>());
    }

    public void addOrderLineItem(final OrderLineItem orderLineItem) {
        this.orderLineItems.add(orderLineItem);
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void changeOrderStatus(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    private void validate(final List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new InvalidTryOrderException("주문하기 위해서는 1개 이상의 주문 항목이 필요합니다.");
        }
    }
}
