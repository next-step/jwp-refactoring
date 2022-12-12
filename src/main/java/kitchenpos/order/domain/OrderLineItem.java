package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;
    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;
    @Column
    private long quantity;

    public OrderLineItem(){}

    private OrderLineItem(OrderLineItemBuilder builder) {
        this.seq = builder.seq;
        this.order = builder.order;
        this.menu = builder.menu;
        this.quantity = builder.quantity;
    }

    public static OrderLineItemBuilder builder() {
        return new OrderLineItemBuilder();
    }

    public static class OrderLineItemBuilder {
        private Long seq;
        private Order order;
        private Menu menu;
        private long quantity;

        public OrderLineItemBuilder seq(Long seq) {
            this.seq = seq;
            return this;
        }

        public OrderLineItemBuilder order(Order order) {
            this.order = order;
            return this;
        }

        public OrderLineItemBuilder menu(Menu menu) {
            this.menu = menu;
            return this;
        }

        public OrderLineItemBuilder quantity(long quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderLineItem build(){
            return new OrderLineItem(this);
        }
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(final Long seq) {
        this.seq = seq;
    }

    public Long getOrderId() {
        return Objects.isNull(order) ? null : order.getId();
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Long getMenuId() {
        return Objects.isNull(menu) ? null : menu.getId();
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }
}
