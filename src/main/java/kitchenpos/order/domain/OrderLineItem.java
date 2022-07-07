package kitchenpos.order.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @Embedded
    private Quantity quantity;

    protected OrderLineItem() {
    }

    private OrderLineItem(OrderLineItem orderLineItem) {
        this(orderLineItem.menu, orderLineItem.quantity);
    }

    public OrderLineItem(Menu menu, long quantity) {
        this.menu = menu;
        this.quantity = Quantity.from(quantity);
    }

    private OrderLineItem(Menu menu, Quantity quantity) {
        this.menu = menu;
        this.quantity = quantity;
    }

    public static OrderLineItem of(Menu menu, Long quantity) {
        return new OrderLineItem(menu, quantity);
    }

    public static OrderLineItem from(OrderLineItem orderLineItem) {
        return new OrderLineItem(orderLineItem);
    }

    public boolean hasSameMenu(OrderLineItem orderLineItem) {
        return menu.equals(orderLineItem.menu);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menu.getId();
    }

    public long getQuantity() {
        return quantity.value();
    }
}
