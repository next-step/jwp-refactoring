package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import kitchenpos.common.domain.Quantity;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne
    private Order order;
    @Column(nullable = false)
    private Long menuId;
    private Quantity quantity;
    
    public OrderLineItem() {
    }

    public OrderLineItem(Long seq, Order order, Long menuId, long quantity) {
        this.seq = seq;
        this.order = order;
        this.menuId = menuId;
        this.quantity = Quantity.from(quantity);
    }

    public static OrderLineItem of(Long menuId, Long quantity) {
        return new OrderLineItem(null, null, menuId, quantity);
    }

    public void setOrder(Order order) {
        this.order = order;
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

    public Quantity getQuantity() {
        return quantity;
    }
}
