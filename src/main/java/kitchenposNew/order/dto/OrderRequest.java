package kitchenposNew.order.dto;

import kitchenposNew.menu.domain.Menu;
import kitchenposNew.order.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenposNew.order.domain.Order;
import kitchenposNew.order.exception.EmptyOrderLineItemsException;
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

    public Order toOrder(OrderTable orderTable, List<Menu> menus) {
        List<OrderLineItem> orderLineItems = toOrderLineItem(menus);
        return new Order(orderTable, orderLineItems);
    }

    private List<OrderLineItem> toOrderLineItem(List<Menu> menus) {
        return menus.stream()
                .map(this::findOrderLineItem)
                .collect(Collectors.toList());
    }

    private OrderLineItem findOrderLineItem(Menu menus) {
        return orderLineItemRequests.stream()
                .filter(orderLineItemRequest -> orderLineItemRequest.getMenuId() == menus.getId())
                .map(orderLineItemRequest -> new OrderLineItem(menus, orderLineItemRequest.getQuantity()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public List<Long> getMenuIds() {
        if(CollectionUtils.isEmpty(orderLineItemRequests)){
            throw new EmptyOrderLineItemsException();
        }
        return orderLineItemRequests.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
    }

    public boolean isEqualsMenuSize(Long countByIdIn) {
        return Long.valueOf(orderLineItemRequests.size()) == countByIdIn;
    }

    public Long getOrderTableId() {
        return orderTableId;
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
