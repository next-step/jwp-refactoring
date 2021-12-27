package order.domain;

import javax.persistence.*;
import menu.domain.*;

@Entity
public class OrderLineItem {
    private static final long QUANTITY_MIN_VALUE = 1L;
    private static final String QUANTITY_IS_LESS_THAN_MIN = "수량이 최소 미만입니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Order order, Menu menu, long quantity) {
        validate(quantity);
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
    }

    private void validate(long quantity) {
        if (quantity < QUANTITY_MIN_VALUE) {
            throw new IllegalArgumentException(QUANTITY_IS_LESS_THAN_MIN);
        }
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

    public long getQuantity() {
        return quantity;
    }
}
