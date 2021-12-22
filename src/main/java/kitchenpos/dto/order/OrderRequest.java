package kitchenpos.dto.order;

import java.security.InvalidParameterException;
import java.util.List;
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

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public List<Long> getMenuIds() {
        return orderLineItems.stream()
            .map(OrderLineItemRequest::getMenuId)
            .collect(Collectors.toList());
    }

    public Long getOrderLineItemQuantity(Long menuId) {
        OrderLineItemRequest orderLineItemRequest = orderLineItems.stream()
            .filter(orderLineItemRequestTarget -> orderLineItemRequestTarget.isSameMenuId(menuId))
            .findFirst()
            .orElseThrow(() -> new InvalidParameterException("메뉴를 찾을 수 없습니다."));

        return orderLineItemRequest.getQuantity();
    }
}
