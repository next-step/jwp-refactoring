package kitchenpos.domain;

import java.util.Objects;

public class OrderLineItem {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    public OrderLineItem(Long seq, Long orderId, Long menuId, long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem(Long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
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

    public long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderLineItem that = (OrderLineItem) o;
        return Objects.equals(seq, that.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }
}
