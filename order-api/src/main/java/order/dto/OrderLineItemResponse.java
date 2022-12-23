package order.dto;

import order.domain.OrderLineItem;

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
        return new OrderLineItemResponse(
                orderLineItem.getSeq(), orderLineItem.getMenuId(), orderLineItem.getQuantity(),
                orderLineItem.getMenuName(), orderLineItem.getPrice().value()
        );
    }

    public long getQuantity() {
        return quantity;
    }
}
