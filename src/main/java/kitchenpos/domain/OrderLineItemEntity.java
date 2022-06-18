package kitchenpos.domain;

import javax.persistence.*;
import static java.util.Objects.requireNonNull;

@Entity(name = "order_line_item")
public class OrderLineItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne
    private Order order;
    @Column(nullable = false)
    private Long menuId;
    @Embedded
    private Quantity quantity;

    public OrderLineItemEntity(Long menuId, long quantity) {
        this.menuId = requireNonNull(menuId, "menuId");
        this.quantity = new Quantity(quantity);
    }

    protected OrderLineItemEntity() {
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
