package kitchenpos.dto;

import kitchenpos.domain.Menu;
import kitchenpos.domain.OrderLineItem;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class OrderRequest {
    private Long orderTableId;
    private String orderStatus;
    private List<OrderLineItemRequest> orderLineItems;

    public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public OrderRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderRequest() {
    }

    public void validateEmptyOrderLineItems() {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문 내역이 없습니다.");
        }
    }

    public void validateExistingSizeMenus(List<Menu> menus) {
        if (orderLineItems.size() != menus.size()) {
            throw new IllegalArgumentException("주문 요청한 메뉴 중에 존재하지 않는 메뉴가 있습니다.");
        }
    }

    public List<OrderLineItem> createOrderLineItems(List<Menu> menus) {
        return orderLineItems.stream()
                .map(orderLineItem -> {
                    Menu menu = menus.stream()
                            .filter(filterMenu -> filterMenu.getId().equals(orderLineItem.getMenuId()))
                            .findFirst()
                            .orElseThrow(IllegalArgumentException::new);

                    return new OrderLineItem(menu, orderLineItem.getQuantity());
                }).collect(Collectors.toList());
    }

    public List<Long> getMenuIds() {
        if(orderLineItems == null) {
            return Collections.emptyList();
        }
        return orderLineItems.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
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
}
