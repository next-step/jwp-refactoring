package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "menu_id")
    private Long menuId;

    private Long quantity;

    protected OrderLineItem() {
    }

    public void registerOrder(Long orderId) {
        this.orderId = orderId;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getMenuId() {
        return getMenuId();
    }

    public Long getQuantity() {
        return quantity;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private OrderLineItem orderLineItem = new OrderLineItem();

        public Builder id(Long seq) {
            orderLineItem.seq = seq;
            return this;
        }

        public Builder orderId(Long orderId) {
            orderLineItem.orderId = orderId;
            return this;
        }

        public Builder menuId(Long menuId) {
            orderLineItem.menuId = menuId;
            return this;
        }

        public Builder quantity(Long quantity) {
            orderLineItem.quantity = quantity;
            return this;
        }

        public OrderLineItem build() {
            return orderLineItem;
        }
    }
}
