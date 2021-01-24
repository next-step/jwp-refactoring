package kitchenpos.order.domain;

import kitchenpos.common.domain.BaseEntity;
import kitchenpos.menu.domain.Menu;

import javax.persistence.*;

@Entity
public class OrderMenu extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;
    @Column
    private long quantity;

    public OrderMenu(Menu menu, Long quantity) {
        this.menu = menu;
        this.quantity = quantity;
    }

    protected OrderMenu() {
    }

    public void changeOrder(Order order) {
        this.order = order;
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
}
