package kitchenpos.order.domain;

import kitchenpos.global.Name;
import kitchenpos.global.Price;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    private Long menuId;

    @Column(name = "menu_name", nullable = false)
    private Name name;

    @Column(name = "menu_price", nullable = false)
    private Price price;

    private Long quantity;

    public OrderLineItem() {
    }

    private OrderLineItem(Long menuId, String name, BigDecimal price, Long quantity) {
        this.menuId = menuId;
        this.name = new Name(name);
        this.price = new Price(price);
        this.quantity = quantity;
    }

    public static OrderLineItem of(Long menuId, String name, BigDecimal price, Long quantity) {
        return new OrderLineItem(menuId, name, price, quantity);
    }

    public void changeOrderLineItem(Order order) {
        this.order = order;
        order.addOrderLineItem(this);
    }

    public Long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
