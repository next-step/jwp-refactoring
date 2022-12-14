package kitchenpos.order.dto;

import kitchenpos.menu.dto.MenuResponse;

public class OrderLineItemResponse {

    private Long seq;
    private MenuResponse menu;
    private Long quantity;

    public OrderLineItemResponse(Long seq, MenuResponse menu, Long quantity) {
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

    public Long getQuantity() {
        return quantity;
    }
}
