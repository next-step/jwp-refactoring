package kitchenpos.domain.order;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import kitchenpos.domain.menu.Menu;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(
        name = "order_id",
        foreignKey = @ForeignKey(name = "FK_ORDER_LINE_ITEM_TO_ORDER"),
        nullable = false
    )
    private Order order;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "menu_id",
        foreignKey = @ForeignKey(name = "FK_ORDER_LINE_ITEM_TO_MENU"),
        nullable = false
    )
    private Menu menu;

    private long quantity;

    public OrderLineItem() {

    }

    public OrderLineItem(Menu menu, long quantity) {
        this.menu = menu;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }


    public long getQuantity() {
        return quantity;
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

    public void setOrder(Order order) {
        this.order = order;
    }
}
