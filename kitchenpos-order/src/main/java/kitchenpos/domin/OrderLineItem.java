package kitchenpos.domin;

import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Quantity;

import javax.persistence.*;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(targetEntity = Order.class, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "order_id")
    private Order order;

    private Long menuId;

    private Quantity quantity;

    protected OrderLineItem() {

    }

    public OrderLineItem(Order order, Long menuId, long quantity) {
        this.order = order;
        this.menuId = menuId;
        this.quantity = new Quantity(quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity.getQuantity();
    }
}
