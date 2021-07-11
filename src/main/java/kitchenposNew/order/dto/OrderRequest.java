package kitchenposNew.order.dto;

import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenposNew.order.domain.Order;
import kitchenposNew.order.exception.EmptyOrderLineItemsException;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderRequest {
    private Long orderTableId;
    private List<OrderLineItem> orderLineItems;

    protected OrderRequest() {
    }

    public OrderRequest(Long orderTableId, List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Order toOrder(OrderTable orderTable) {
        return new Order(orderTable, orderLineItems);
    }

    public List<Long> getMenuIds() {
        if(CollectionUtils.isEmpty(orderLineItems)){
            throw new EmptyOrderLineItemsException();
        }
        return orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    public boolean isEqualsMenuSize(Long countByIdIn) {
        return Long.valueOf(orderLineItems.size()) == countByIdIn;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderRequest that = (OrderRequest) o;
        return Objects.equals(orderTableId, that.orderTableId) && Objects.equals(orderLineItems, that.orderLineItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderTableId, orderLineItems);
    }
}
