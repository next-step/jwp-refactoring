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

    private long quantity;

    protected OrderLineItem() {

    }

    public OrderLineItem(Order order, Menu menu, long quantity) {
        if (Objects.isNull(order)) {
            throw new IllegalArgumentException();
        }
        if (Objects.isNull(menu)) {
            throw new IllegalArgumentException();
        }
        if (quantity < 0 ){
            throw new IllegalArgumentException();
        }

        updateOrder(order);
        this.menu = menu;
        this.quantity = quantity;
    }

    public void updateOrder(Order order) {
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

    public long getQuantity() {
        return quantity;
    }
}
