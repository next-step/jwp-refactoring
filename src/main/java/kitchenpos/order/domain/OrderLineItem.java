package kitchenpos.order.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import kitchenpos.menu.domain.Menu;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;
    @Embedded
    private OrderLineItemQuantity quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Menu menu, OrderLineItemQuantity quantity) {
        this.menu = menu;
        this.quantity = quantity;
    }

    public OrderLineItem(Long seq, Order order, Menu menu, OrderLineItemQuantity quantity) {
        this.seq = seq;
        this.menu = menu;
        this.quantity = quantity;
    }

    public Menu getMenu() {
        return menu;
    }

    public OrderLineItemQuantity getQuantity(){
        return quantity;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }
}
