package kitchenpos.order.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import kitchenpos.common.domian.Quantity;

@Entity
@Table(name = "order_line_item")
public class OrderLineItem {

    public OrderLineItem() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private Order order;

    @JoinColumn(name = "menu_id")
    private Long menuId;

    @Embedded
    private Quantity quantity;

    private OrderLineItem(Order order, Long menuId, Quantity quantity) {
        this.order = order;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem(Long seq, Order order, Long menuId, Quantity quantity) {
        this.seq = seq;
        this.order = order;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItem of(Order order, Long menuId, Quantity quantity) {
        return new OrderLineItem(order, menuId, quantity);
    }

    public Long seq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long quantityToLong() {
        return quantity.amount();
    }
}
