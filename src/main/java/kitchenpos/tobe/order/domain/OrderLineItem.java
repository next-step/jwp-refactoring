package kitchenpos.tobe.order.domain;

import kitchenpos.tobe.menu.domain.Menu;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_line_item_orders"))
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_order_line_item_menu"))
    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    private Long quantity;

    protected OrderLineItem() {
    }

    public void registerOrder(Order order) {
        this.order = order;
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public Menu getMenu() {
        return menu;
    }

    public Long getMenuId() {
        return menu.getId();
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

        public Builder order(Order order) {
            orderLineItem.order = order;
            return this;
        }

        public Builder menu(Menu menu) {
            orderLineItem.menu = menu;
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
