package kitchenpos.order.dto;

import java.math.BigDecimal;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {

    private Long seq;
    private Long menuId;
    private String menuName;
    private BigDecimal menuPrice;
    private long quantity;


    public OrderLineItemResponse() {}

    public OrderLineItemResponse(Long seq, Long menuId, String menuName, BigDecimal menuPrice, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse from(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getSeq(), orderLineItem.getMenuId(), orderLineItem.getMenuNameValue(),
                orderLineItem.getMenuPriceValue(),
                orderLineItem.getQuantityValue());
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
