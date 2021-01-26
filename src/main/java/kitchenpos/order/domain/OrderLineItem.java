package kitchenpos.order.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kitchenpos.menu.domain.Menu;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;
    private long quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(Long seq, Order order, Menu menu, long quantity) {
        this.seq = seq;
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
    }

    public Order getOrder() {
        return order;
    }

    public Menu getMenu() {
        return menu;
    }

    public long getQuantity() {
        return quantity;
    }

    public void belongToOrder(Order order) {
        this.order = order;
    }

    public static final class Builder {
        private Long seq;
        private Order order;
        private Menu menu;
        private long quantity;

        public Builder() {
        }

        public Builder seq(Long seq) {
            this.seq = seq;
            return this;
        }

        public Builder order(Order order) {
            this.order = order;
            return this;
        }

        public Builder menu(Menu menu) {
            this.menu = menu;
            return this;
        }

        public Builder quantity(long quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderLineItem build() {
            return new OrderLineItem(seq, order, menu, quantity);
        }
    }
}
