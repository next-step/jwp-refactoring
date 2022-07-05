package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {
    private Long seq;
    private String name;
    private long price;
    private long quantity;

    public OrderLineItemResponse(OrderLineItem orderLineItem) {
        seq = orderLineItem.getSeq();
        name = orderLineItem.getOrderMenu().getName();
        price = orderLineItem.getOrderMenu().getPrice().getValue();
        quantity = orderLineItem.getQuantity();
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
}
