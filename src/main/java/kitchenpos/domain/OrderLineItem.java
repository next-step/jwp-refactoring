package kitchenpos.domain;

import kitchenpos.dto.OrderLineItemRequest;

import javax.persistence.*;
import java.util.List;

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


    public OrderLineItem(Long seq, Order order, Menu menu, long quantity) {
        this.seq = seq;
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
    }

    private OrderLineItem(Order order, Menu menu, long quantity) {
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
    }

    public static OrderLineItem of(Long quantity, List<Menu> menus, Order order, Long menuId) {
        Menu filterMenu = menus.stream()
                .filter(menu -> menu.getId().equals(menuId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

        return new OrderLineItem(order, filterMenu, quantity);
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
