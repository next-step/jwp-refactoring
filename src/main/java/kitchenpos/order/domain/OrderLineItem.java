package kitchenpos.order.domain;

import kitchenpos.price.domain.Quantity;

import javax.persistence.*;

import static java.util.Objects.requireNonNull;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_order_line_item_to_order"))
    private Order order;

    @Column(nullable = false)
    private Long menuId;

    @Embedded
    private Quantity quantity;

    public OrderLineItem(Long menuId, long quantity) {
        this.menuId = requireNonNull(menuId, "menuId");
        this.quantity = new Quantity(quantity);
    }

    protected OrderLineItem() {
    }

    public void bindTo(Order order) {
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
