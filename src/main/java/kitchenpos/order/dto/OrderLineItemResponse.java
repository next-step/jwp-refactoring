package kitchenpos.order.dto;

import java.math.BigDecimal;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineMenu;

public class OrderLineItemResponse {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private String menuName;
    private BigDecimal menuPrice;
    private long quantity;

    private OrderLineItemResponse() {
    }


    public OrderLineItemResponse(Long menuId, String menuName, BigDecimal menuPrice, long quantity) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse of(OrderLineItem orderLineItem) {
        OrderLineMenu orderLineMenu = orderLineItem.getOrderLineMenu();
        return new OrderLineItemResponse(
                orderLineMenu.getMenuId()
                , orderLineMenu.getMenuName()
                , orderLineMenu.getMenuPrice()
                , orderLineItem.getQuantity()
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

    public long getQuantity() {
        return quantity;
    }
}
