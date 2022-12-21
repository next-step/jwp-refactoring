package kitchenpos.order.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
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
    @Embedded
    @AttributeOverride(name = "menuId", column = @Column(name = "menu_id"))
    @AttributeOverride(name = "menuName.name", column = @Column(name = "menu_name"))
    @AttributeOverride(name = "menuPrice.price", column = @Column(name = "menu_price"))
    private OrderMenu orderMenu;
    private Quantity quantity;
    
    public OrderLineItem() {
    }

    public OrderLineItem(Long seq, Order order, OrderMenu orderMenu, long quantity) {
        this.seq = seq;
        this.order = order;
        this.orderMenu = orderMenu;
        this.quantity = Quantity.from(quantity);
    }

    public static OrderLineItem of(OrderMenu orderMenu, Long quantity) {
        return new OrderLineItem(null, null, orderMenu, quantity);
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

    public OrderMenu getOrderMenu() {
        return orderMenu;
    }

    public Quantity getQuantity() {
        return quantity;
    }
}
