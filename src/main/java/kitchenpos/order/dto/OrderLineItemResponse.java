package kitchenpos.order.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.order.domain.OrderLineItem;

import java.util.Map;

public class OrderLineItemResponse {

    private Long seq;
    private MenuResponse menu;
    private Long quantity;

    public OrderLineItemResponse(Long seq, MenuResponse menu, Long quantity) {
        this.seq = seq;
        this.menu = menu;
        this.quantity = quantity;
    }


    public static OrderLineItemResponse of(OrderLineItem orderLineItem, Menu menu) {
        return new OrderLineItemResponse(orderLineItem.getSeq(),
                MenuResponse.of(menu),
                orderLineItem.getQuantity().getValue());
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
