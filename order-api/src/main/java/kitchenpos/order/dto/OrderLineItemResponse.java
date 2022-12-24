package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;

import java.math.BigDecimal;

public class OrderLineItemResponse {
    private final Long seq;
    private final Long menuId;
    private final long quantity;
    private final String menuName;
    private final BigDecimal menuPrice;

    public OrderLineItemResponse(final Long seq, final Long menuId, final long quantity, final String menuName, final BigDecimal menuPrice) {
        this.seq = seq;
        this.menuId = menuId;
        this.quantity = quantity;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
    }

    public static OrderLineItemResponse from(final OrderLineItem orderLineItem) {
        OrderMenu orderMenu = orderLineItem.getOrderMenu();
        return new OrderLineItemResponse(
                orderLineItem.getSeq(), orderMenu.getMenuId(), orderLineItem.getQuantity(),
                orderMenu.getName(), orderMenu.getPrice().value()
        );
    }

    public long getQuantity() {
        return quantity;
    }
}
