package kitchenpos.order.domain;

import kitchenpos.menu.domain.Quantity;

import javax.persistence.*;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_line_item_orders"))
    private Order order;

    @Column(nullable = false)
    private Long menuId;

    @Embedded
    private Quantity quantity;

    protected OrderLineItem() {

    }

    public OrderLineItem(Long menuId, Quantity quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItem of(Long menuId, Long quantity) {
        return new OrderLineItem(menuId, Quantity.of(quantity));
    }

    public void changeOrder(Order order) {
        this.order = order;
    }

    public Long getMenuId() {
        return this.menuId;
    }

    public Quantity getQuantity() {
        return this.quantity;
    }
}
