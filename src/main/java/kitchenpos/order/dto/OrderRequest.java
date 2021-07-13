package kitchenpos.order.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.order.domain.Order;
import kitchenpos.order.exception.EmptyOrderLineItemsException;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderRequest {
    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItemRequests;

    protected OrderRequest() {
    }

    public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItemRequests) {
        this.orderTableId = orderTableId;
        this.orderLineItemRequests = orderLineItemRequests;
    }

    public List<Long> getMenuIds() {
        if(CollectionUtils.isEmpty(orderLineItemRequests)){
            throw new EmptyOrderLineItemsException();
        }
        return orderLineItemRequests.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
    }

    public boolean isEqualsMenuSize(long countByIdIn) {
        return orderLineItemRequests.size() == countByIdIn;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItemRequests() {
        return orderLineItemRequests;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderRequest that = (OrderRequest) o;
        return Objects.equals(orderTableId, that.orderTableId) && Objects.equals(orderLineItemRequests, that.orderLineItemRequests);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderTableId, orderLineItemRequests);
    }
}
