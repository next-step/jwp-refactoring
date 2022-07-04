package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {
    private Long seq;
    private Long menuId;
    private String menuName;
    private Long menuPrice;
    private int quantity;

    public OrderLineItemResponse() {
    }

    public OrderLineItemResponse(final Long seq,
                                 final Long menuId,
                                 final String menuName,
                                 final Long menuPrice,
                                 final int quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse of(final OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(
                orderLineItem.getSeq(),
                orderLineItem.getMenuId(),
                orderLineItem.getMenuName(),
                orderLineItem.getMenuPrice(),
                orderLineItem.getQuantity().value());
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

    public Long getMenuPrice() {
        return menuPrice;
    }

    public int getQuantity() {
        return quantity;
    }
}
