package kitchenpos.order.ui.dto;

import kitchenpos.order.domain.OrderLineItem;

import java.math.BigDecimal;

public class OrderLineItemResponse {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private String menuName;
    private BigDecimal menuPrice;
    private long quantity;

    private OrderLineItemResponse() {
    }

    public OrderLineItemResponse(OrderLineItem orderLineItem) {
        this.seq = orderLineItem.getSeq();
        this.orderId = orderLineItem.getOrderId();
        this.menuId = orderLineItem.getMenuId();
        this.menuName = orderLineItem.getOrderMenu().getName();
        this.menuPrice = orderLineItem.getOrderMenu().getPrice().value();
        this.quantity = orderLineItem.getQuantity();
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

    public long getQuantity() {
        return quantity;
    }
}
