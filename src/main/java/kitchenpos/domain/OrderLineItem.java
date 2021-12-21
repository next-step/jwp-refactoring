package kitchenpos.domain;


import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
public class OrderLineItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_line_item_orders"))
    private Order order;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_order_line_item_menu"))
    private Menu menu;

    @Embedded
    private Quantity quantity;

    protected OrderLineItem() {
    }

    public Long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public Menu getMenu() {
        return menu;
    }

    public Quantity getQuantity() {
        return quantity;
    }
}
