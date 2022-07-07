package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;

import java.util.Objects;

public class OrderLineItemResponse {
    private final Long seq;
    private final String name;
    private final long price;
    private final long quantity;

    public OrderLineItemResponse(final Long seq, final String name, final long price, final long quantity) {
        this.seq = seq;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse of(final OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getSeq(), orderLineItem.getMenuName(),
                orderLineItem.getMenuPrice(), orderLineItem.getQuantity());
    }

    public Long getSeq() {
        return seq;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public long getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "OrderLineItemResponse{" +
                "seq=" + seq +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OrderLineItemResponse that = (OrderLineItemResponse) o;
        return price == that.price && quantity == that.quantity && Objects.equals(seq, that.seq) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, name, price, quantity);
    }
}
