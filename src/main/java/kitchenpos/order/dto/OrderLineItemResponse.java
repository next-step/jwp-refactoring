package kitchenpos.order.dto;

import java.math.BigDecimal;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {
    private final Long seq;
    private final Long menuId;
    private final String menuName;
    private final BigDecimal menuPrice;
    private final long quantity;

    private OrderLineItemResponse(Long seq, Long menuId, String menuName, BigDecimal menuPrice, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse from(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(
            orderLineItem.getSeq(),
            orderLineItem.getMenuId(),
            orderLineItem.getMenuName(),
            orderLineItem.getMenuPrice(),
            orderLineItem.getQuantity()
        );
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public BigDecimal getMenuPrice() {
        return menuPrice;
    }

    public long getQuantity() {
        return quantity;
    }
}
