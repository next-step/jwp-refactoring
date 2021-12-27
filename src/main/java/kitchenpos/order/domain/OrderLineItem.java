package kitchenpos.order.domain;

import kitchenpos.common.domain.Quantity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @Column
    private Long menuId;

    @Embedded
    private Quantity quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Long menuId, Quantity quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem(Order order, Long menuId, Quantity quantity) {
        this.order = order;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem(Long id, Order order, Long menuId, Quantity quantity) {
        this(order, menuId, quantity);
        this.id = id;
    }

    public static OrderLineItem of(Long menuId, Quantity quantity) {
        return new OrderLineItem(menuId, quantity);
    }

    public static OrderLineItem of(Order order, Long menuId, Quantity quantity) {
        return new OrderLineItem(order, menuId, quantity);
    }

    public static OrderLineItem of(Long id, Order order, Long menuId, Quantity quantity) {
        return new OrderLineItem(id, order, menuId, quantity);
    }

    public Long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Quantity getQuantity() {
        return quantity;
    }
}
