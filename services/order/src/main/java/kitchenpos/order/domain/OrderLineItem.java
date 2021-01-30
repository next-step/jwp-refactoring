package kitchenpos.order.domain;

import javax.persistence.Embedded;
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
    @JoinColumn(name = "menu_id")
    private Menu menu;
    @Embedded
    private OrderLineItemQuantity orderLineItemQuantity;

    public OrderLineItem() {
    }

    public OrderLineItem( Menu menu, long quantity) {
        this.menu = menu;
        this.orderLineItemQuantity = new OrderLineItemQuantity(quantity);
    }

    public Menu getMenu() {
        return menu;
    }

    public long getQuantity() {
        return orderLineItemQuantity.getValue();
    }

    public static final class Builder {
        private Menu menu;
        private long quantity;

        public Builder() {
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
            return new OrderLineItem(menu, quantity);
        }
    }
}
