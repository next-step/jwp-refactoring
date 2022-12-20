package kitchenpos.order.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.product.domain.MenuProducts;

import java.util.Arrays;
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


    public static OrderLineItemResponse of(OrderLineItem orderLineItem, Menu menu, MenuProducts menuProducts) {
        return new OrderLineItemResponse(orderLineItem.getSeq(),
                MenuResponse.of(menu, menuProducts),
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
