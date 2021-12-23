package kitchenpos.order.dto;

import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {

    private Long seq;
    private MenuResponse menu;
    private long quantity;

    private OrderLineItemResponse() {
    }

    public OrderLineItemResponse(Long seq, MenuResponse menu, long quantity) {
        this.seq = seq;
        this.menu = menu;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public MenuResponse getMenu() {
        return menu;
    }

    public long getQuantity() {
        return quantity;
    }

    public static OrderLineItemResponse from(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getSeq(),
            MenuResponse.from(orderLineItem.getMenu()),
            orderLineItem.getQuantity());
    }
}
