package kitchenpos.order.dto;

import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class OrderRequest {
    private static final String EXCEPTION_MESSAGE_INVALID_MENU_PRODUCTS = "유효한 Order Line Item 이 없습니다! 값을 확인해주세요.";
    private static final String EXCEPTION_MESSAGE_MENU_ID_DUPLICATED = "동일한 주문 메뉴가 여러개 입니다.";
    private Long orderTableId;
    private String orderStatus;
    private List<OrderLineItemRequest> orderLineItems;

    private OrderRequest() {
    }

    public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        validateOrderLineItems(orderLineItems);
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public OrderRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public Long getQuantityByMenuId(Long menuId) {
        return orderLineItems.stream()
                .filter(orderLineItemRequest -> orderLineItemRequest.getMenuId().equals(menuId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .getQuantity();
    }

    private void validateOrderLineItems(List<OrderLineItemRequest> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException(EXCEPTION_MESSAGE_INVALID_MENU_PRODUCTS);
        }
        if (isDuplicateMenuId(orderLineItems)) {
            throw new IllegalArgumentException(EXCEPTION_MESSAGE_MENU_ID_DUPLICATED);
        }
    }

    private boolean isDuplicateMenuId(List<OrderLineItemRequest> orderLineItems) {
        Set<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toSet());
        return menuIds.size() != orderLineItems.size();
    }

}
