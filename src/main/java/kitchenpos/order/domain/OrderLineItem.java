package kitchenpos.order.domain;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private Order order;

    @Embedded
    private MenuId menuId;

    private Long quantity;

    public static OrderLineItem of(MenuId menuId, long quantity) {
        return new OrderLineItem(null, menuId, quantity);
    }

    public static OrderLineItem of(Long id, MenuId menuId, long quantity) {
        return new OrderLineItem(id, menuId, quantity);
    }

    public OrderLineItem(Long id, MenuId menuId, Long quantity) {
        this.id = id;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    protected OrderLineItem() {
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public MenuId getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
