package kitchenpos.order.dto;

import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.order.domain.OrderLineItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderLineItemResponse {
    private Long seq;
    private MenuResponse menuResponse;
    private Long quantity;

    protected OrderLineItemResponse() {
    }

    public OrderLineItemResponse(Long seq, MenuResponse menuResponse, Long quantity) {
        this.seq = seq;
        this.menuResponse = menuResponse;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse of(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getSeq(), MenuResponse.of(orderLineItem.getMenu()), orderLineItem.getQuantity());
    }

    public static List<OrderLineItemResponse> ofList(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItemResponse::of)
                .collect(Collectors.toList());
    }

    public Long getSeq() {
        return seq;
    }

    public MenuResponse getMenuResponse() {
        return menuResponse;
    }

    public Long getQuantity() {
        return quantity;
    }
}
