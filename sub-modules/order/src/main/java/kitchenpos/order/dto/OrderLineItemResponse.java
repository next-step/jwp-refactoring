package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;

import java.math.BigDecimal;

public class OrderLineItemResponse {
    private Long seq;
    private Long menuId;
    private String menuName;
    private BigDecimal menuPrice;
    private Long quantity;

    private OrderLineItemResponse() {
    }

    private OrderLineItemResponse(Long seq, Long menuId, String menuName, BigDecimal menuPrice, Long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse of(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getSeq(),
                orderLineItem.getMenuId(),
                orderLineItem.getMenuName().value(),
                orderLineItem.getMenuPrice().value(),
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

    public Long getQuantity() {
        return quantity;
    }
}
