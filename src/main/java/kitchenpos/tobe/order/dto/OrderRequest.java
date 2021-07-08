package kitchenpos.tobe.order.dto;

import kitchenpos.tobe.menu.domain.Menu;
import kitchenpos.tobe.order.domain.OrderLineItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderRequest {
    private final Long orderTableId;
    private final List<OrderLineItemRequest> orderLineItemRequests;

    public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItemRequests) {
        this.orderTableId = orderTableId;
        this.orderLineItemRequests = orderLineItemRequests;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItemRequests() {
        return orderLineItemRequests;
    }

    public List<OrderLineItem> getOrderLineItem() {
        return orderLineItemRequests
                .stream()
                .map(v -> {
                    Long quantity = v.getQuantity();
                    Menu menu = Menu.builder().id(v.getMenuId()).builder();

                    return OrderLineItem.builder()
                            .menu(menu)
                            .quantity(quantity)
                            .build();
                }).collect(Collectors.toList());
    }

    public List<Long> getMenuIds() {
        return orderLineItemRequests.stream()
                .map(v -> v.getMenuId())
                .collect(Collectors.toList());
    }
}
