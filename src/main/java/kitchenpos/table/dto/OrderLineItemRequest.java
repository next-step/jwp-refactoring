package kitchenpos.domain;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemRequest {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    public OrderLineItemRequest() {
	}

	public OrderLineItemRequest(Long menuId, long quantity) {
		this.menuId = menuId;
		this.quantity = quantity;
	}

	public OrderLineItemRequest(Long orderId, Long menuId, long quantity) {
    	this.orderId = orderId;
    	this.menuId = menuId;
    	this.quantity = quantity;
	}

    public Long getSeq() {
        return seq;
    }

    public void setSeq(final Long seq) {
        this.seq = seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(final Long orderId) {
        this.orderId = orderId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(final Long menuId) {
        this.menuId = menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }

    public OrderLineItem toOrderLineItem(){
    	return new OrderLineItem();
	}
}
