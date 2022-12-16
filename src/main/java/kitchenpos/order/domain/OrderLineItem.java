package kitchenpos.order.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import kitchenpos.common.domain.Quantity;

@Entity
@Table(name = "order_line_item")
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Embedded
    private OrderMenu orderMenu;

    @Embedded
    private Quantity quantity;

    protected OrderLineItem() {}

    public OrderLineItem(Long seq, Order order, OrderMenu orderMenu, long quantity) {
        this.seq = seq;
        this.order = order;
        this.orderMenu = orderMenu;
        this.quantity = Quantity.from(quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public OrderMenu getOrderMenu() {
        return orderMenu;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public long getQuantityValue() {
        return quantity.value();
    }

    public Long getOrderId() {
        return order.getId();
    }

    public Long getMenuId() {
        return orderMenu.getMenuId();
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public static class Builder {

        private Long seq;
        private Order order;
        private OrderMenu orderMenu;
        private long quantity;

        public Builder seq(Long seq) {
            this.seq = seq;
            return this;
        }

        public Builder order(Order order) {
            this.order = order;
            return this;
        }

        public Builder orderMenu(OrderMenu orderMenu) {
            this.orderMenu = orderMenu;
            return this;
        }

        public Builder quantity(long quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderLineItem build() {
            return new OrderLineItem(seq, order, orderMenu, quantity);
        }
    }
}
