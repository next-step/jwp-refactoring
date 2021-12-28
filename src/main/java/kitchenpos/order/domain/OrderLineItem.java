package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;

import javax.persistence.*;

@Table(name = "order_line_item")
@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @Embedded
    private OrderQuantity orderQuantity;

    protected OrderLineItem() {

    }

    private OrderLineItem(Menu menu, long quantity) {
        this.menu = menu;
        this.orderQuantity = new OrderQuantity(quantity);
    }

    public static OrderLineItem create(Menu menu, long quantity) {
        return new OrderLineItem(menu, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Menu getMenu() {
        return menu;
    }

    public long getQuantity() {
        return orderQuantity.getQuantity();
    }

}
