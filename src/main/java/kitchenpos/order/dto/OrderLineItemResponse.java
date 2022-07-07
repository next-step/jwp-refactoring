package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;

import java.math.BigDecimal;

public class OrderLineItemResponse {
    private final Long seq;
    private final Long orderId;
    private final Long menuId;
    private final String menuName;
    private final BigDecimal menuPrice;
    private final Long quantity;

    public OrderLineItemResponse(Long seq, Long orderId, Long menuId, String menuName, BigDecimal menuPrice, Long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse of(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(
                orderLineItem.getSeq(),
                orderLineItem.getOrder().getId(),
                orderLineItem.getOrderMenu().getMenuId(),
                orderLineItem.getOrderMenu().getMenuName().getName(),
                orderLineItem.getOrderMenu().getMenuPrice().getPrice(),
                orderLineItem.getQuantity().getQuantity()
        );
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
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
