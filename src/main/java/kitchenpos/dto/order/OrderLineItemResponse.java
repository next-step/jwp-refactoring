package kitchenpos.dto.order;

import kitchenpos.domain.OrderLineItem;
import kitchenpos.dto.menu.MenuResponse;

import java.util.Objects;

public class OrderLineItemResponse {
    private Long seq;
    private MenuResponse menuResponse;
    private long quantity;

    protected OrderLineItemResponse() {
    }

    private OrderLineItemResponse(final Long seq, final MenuResponse menuResponse, final long quantity) {
        this.seq = seq;
        this.menuResponse = menuResponse;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse from(final OrderLineItem orderLineItem) {
        MenuResponse menuResponse = new MenuResponse();
        if (Objects.nonNull(orderLineItem.getMenu())) {
            menuResponse = MenuResponse.from(orderLineItem.getMenu());
        }
        return new OrderLineItemResponse(orderLineItem.getSeq(), menuResponse, orderLineItem.getQuantity().toLong());
    }

    public Long getSeq() {
        return seq;
    }

    public MenuResponse getMenuResponse() {
        return menuResponse;
    }

    public long getQuantity() {
        return quantity;
    }
}
