package kitchenpos.order.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

public class OrderRequest {
    private Long orderTableId;
    private OrderStatus orderStatus;
    private List<OrderLineItemRequest> orderLineItems;

    public Long getOrderTableId() {
        return orderTableId;
    }

    public void setOrderTableId(final Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(final OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public void setOrderLineItems(final List<OrderLineItemRequest> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public OrderRequest() {
    }

    public OrderRequest(Long orderTableId, OrderStatus orderStatus) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
    }

    public List<Long> getMenuIds() {
        return orderLineItems.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
    }

    public void validateSameSizeMenus(List<Menu> menus) {
        if (orderLineItems.size() != menus.size()) {
            throw new IllegalArgumentException("주문 항목에 등록하지 않은 메뉴가 있습니다.");
        }
    }

    public void validateEmptyOrderLineItems() {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문 항목이 비어있습니다.");
        }
    }

    public List<OrderLineItem> createOrderLineItems(List<Menu> menus) {
        return orderLineItems.stream()
                .map(orderLineItem -> OrderLineItem.createOrderLineItem(null,
                        menus.stream()
                                .filter(menu -> menu.isSameById(orderLineItem.getMenuId()))
                                .findFirst()
                                .orElseThrow(IllegalArgumentException::new),
                        orderLineItem.getQuantity()
                )).collect(Collectors.toList());
    }
}
