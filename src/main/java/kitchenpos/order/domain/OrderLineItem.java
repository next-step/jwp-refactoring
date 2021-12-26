package kitchenpos.order.domain;

import kitchenpos.common.domain.BaseEntity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.common.domain.Quantity;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderLineItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @Embedded
    private Quantity quantity;

    protected OrderLineItem() {}

    private OrderLineItem(Long seq, Order order, Menu menu, Quantity quantity) {
        this.seq = seq;
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
    }

    public static OrderLineItem of(Menu menu, Quantity quantity) {
        return new OrderLineItem(null, null, menu, quantity);
    }

    public void addOrder(Order order) {
        if (!equalsOrder(order)) {
            this.order = order;
        }
    }

    private boolean equalsOrder(Order order) {
        if (Objects.isNull(this.order)) {
            return false;
        }

        return this.order.equals(order);
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

    public Long getQuantity() {
        return quantity.get();
    }
}
