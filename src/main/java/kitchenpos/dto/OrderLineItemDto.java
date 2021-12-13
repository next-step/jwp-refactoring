package kitchenpos.dto;

import java.util.Objects;

import kitchenpos.domain.order.OrderLineItem;

public class OrderLineItemDto {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    protected OrderLineItemDto() {
    }

    private OrderLineItemDto(Long seq, Long orderId, Long menuId, long quantity) {
        this.seq= seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemDto of(Long menuId, long quantity) {
        return new OrderLineItemDto(null, null, menuId, quantity);
    }

    public static OrderLineItemDto of(OrderLineItem orderLineItem) {
        return new OrderLineItemDto(orderLineItem.getSeq(), orderLineItem.getOrder().getId(), orderLineItem.getMenu().getId(), orderLineItem.getQuantity());
    }

    public Long getSeq() {
        return this.seq;
    }

    public Long getOrderId() {
        return this.orderId;
    }

    public Long getMenuId() {
        return this.menuId;
    }

    public long getQuantity() {
        return this.quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof OrderLineItemDto)) {
            return false;
        }
        OrderLineItemDto orderLineItemDto = (OrderLineItemDto) o;
        return Objects.equals(seq, orderLineItemDto.seq) && Objects.equals(orderId, orderLineItemDto.orderId) && Objects.equals(menuId, orderLineItemDto.menuId) && quantity == orderLineItemDto.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, orderId, menuId, quantity);
    }

}
