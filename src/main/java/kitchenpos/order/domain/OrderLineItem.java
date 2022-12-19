package kitchenpos.order.domain;

import kitchenpos.global.domain.Price;
import kitchenpos.global.domain.Quantity;

import javax.persistence.*;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_line_item_orders"))
    private Order order;

    @Embedded
    private OrderMenu menu;

    @Embedded
    private Quantity quantity;

    protected OrderLineItem() {

    }

    public OrderLineItem(Order order, OrderMenu menu, Quantity quantity) {
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
    }

    public static OrderLineItem of(Long menuId, String menuName, Long price, Long quantity) {
        return of(menuId, menuName, Price.of(price), quantity);
    }

    public static OrderLineItem of(Long menuId, String menuName, Price price, Long quantity) {
        return new OrderLineItem(
                null,
                OrderMenu.of(menuId, menuName, price),
                Quantity.of(quantity)
        );
    }

    public void changeOrder(Order order) {
        this.order = order;
    }

    public Long getMenuId() {
        return this.menu.getId();
    }

    public Quantity getQuantity() {
        return this.quantity;
    }
}
