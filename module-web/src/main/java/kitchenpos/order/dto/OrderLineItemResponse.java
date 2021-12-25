package kitchenpos.order.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.OrderLineItem;
import kitchenpos.order.OrderMenu;

public class OrderLineItemResponse {

    private Long seq;
    private String menuName;
    private BigDecimal menuPrice;
    private long quantity;

    public OrderLineItemResponse() {
    }

    private OrderLineItemResponse(Long seq, OrderMenu menu, long quantity) {
        this.seq = seq;
        this.menuName = menu.getMenuName();
        this.menuPrice = menu.getMenuPrice();
        this.quantity = quantity;
    }

    public static OrderLineItemResponse of(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getSeq(),
            orderLineItem.getOrderMenu(),
            orderLineItem.getQuantity());
    }

    public static List<OrderLineItemResponse> ofList(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
            .map(OrderLineItemResponse::of)
            .collect(Collectors.toList());
    }

    public Long getSeq() {
        return seq;
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
