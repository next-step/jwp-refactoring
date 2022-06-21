package kitchenpos.dto;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemResponse {
    private Long seq;
    private MenuResponse menu;
    private long quantity;

    public OrderLineItemResponse(OrderLineItem orderLineItem) {
        this.seq = orderLineItem.getSeq();
        this.menu = new MenuResponse(orderLineItem.getMenu());
        this.quantity = orderLineItem.getQuantity();
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
}
