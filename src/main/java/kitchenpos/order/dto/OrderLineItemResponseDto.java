package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponseDto {
    private Long seq;
    private Long menuId;
    private long quantity;

    public OrderLineItemResponseDto(OrderLineItem orderLineItem) {
        this.seq = orderLineItem.getSeq();
        this.menuId = orderLineItem.getMenuId();
        this.quantity = orderLineItem.getQuantity();
    }

    public OrderLineItemResponseDto(Long seq, Long menuId, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
