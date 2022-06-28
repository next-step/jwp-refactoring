package kitchenpos.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderRequest {
    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItems;

    public OrderRequest() {
    }

    public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public void validate(long menuCount) {
        if (orderLineItems.size() != menuCount) {
            throw new IllegalArgumentException();
        }
    }

    public List<Long> getMenuIds() {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
        return orderLineItems.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
    }

    public Order toOrder() {
        Order order = new Order(orderTableId);
        order.addOrderLineItems(orderLineItems);
        return order;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderRequest that = (OrderRequest) o;
        return Objects.equals(getOrderTableId(), that.getOrderTableId()) && Objects.equals(getOrderLineItems(), that.getOrderLineItems());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrderTableId(), getOrderLineItems());
    }
}
