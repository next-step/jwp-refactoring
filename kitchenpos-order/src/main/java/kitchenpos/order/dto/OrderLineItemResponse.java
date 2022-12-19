package kitchenpos.order.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {
    private final long seq;
    private final long menuId;
    private final String menuName;
    private final long quantity;

    private final BigDecimal menuPrice;

    public OrderLineItemResponse(long seq, long menuId, String menuName, long quantity, BigDecimal menuPrice) {
        this.seq = seq;
        this.menuId = menuId;
        this.menuName = menuName;
        this.quantity = quantity;
        this.menuPrice = menuPrice;
    }

    public static OrderLineItemResponse from(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.seq(), orderLineItem.menuId(),
                orderLineItem.menu().nameValue(), orderLineItem.quantityValue(), orderLineItem.menu().priceValue());
    }

    public static List<OrderLineItemResponse> toList(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItemResponse::from)
                .collect(Collectors.toList());
    }

    public long getSeq() {
        return seq;
    }

    public long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public String getMenuName() {
        return menuName;
    }

    public BigDecimal getMenuPrice() {
        return menuPrice;
    }
}
