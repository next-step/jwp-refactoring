package kitchenpos.domain.order;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.Quantity;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class OrderLineMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Quantity quantity;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders orders;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    protected OrderLineMenu() {}

    public OrderLineMenu(Orders orders, Menu menu, Quantity quantity) {
        this.orders = orders;
        checkMenu(menu);
        this.menu = menu;
        this.quantity = quantity;
    }

    private void checkMenu(Menu menu) {
        if (menu == null) {
            throw new IllegalArgumentException("없는 메뉴 입니다");
        }
    }

    public Long getId() {
        return id;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public Menu getMenu() {
        return menu;
    }
}
