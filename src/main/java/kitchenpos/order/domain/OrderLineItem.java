package kitchenpos.order.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.common.domain.Quantity;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "menu_id")),
            @AttributeOverride(name = "name.name", column = @Column(name = "menu_name")),
            @AttributeOverride(name = "price.price", column = @Column(name = "menu_price"))
    })
    private OrderMenu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    private Quantity quantity;

    protected OrderLineItem() {}

    public OrderLineItem(Long seq, Quantity quantity, OrderMenu menu) {
        this.seq = seq;
        this.quantity = quantity;
        this.menu = menu;
    }

    public OrderLineItem(Quantity quantity, OrderMenu menu) {
        this.quantity = quantity;
        this.menu = menu;
    }

    public Long getSeq() {
        return seq;
    }

    public OrderMenu getMenu() {
        return menu;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(final Order order) {
        this.order = order;
    }

    public void addOrder(final Order order) {
        this.order = order;
    }

    public Long getQuantity() {
        return quantity.value();
    }
}
