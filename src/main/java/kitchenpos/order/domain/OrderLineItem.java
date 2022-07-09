package kitchenpos.order.domain;

import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.Quantity;

import javax.persistence.*;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    private Order order;

    @Column(nullable = false)
    private Long menuId;

    @Embedded
    private Quantity quantity;

    @Embedded
    private Price orderedMenuPrice;

    @Column(nullable = false)
    private String menuName;

    protected OrderLineItem() {}

    public OrderLineItem(
            Long seq, Order order, Long menuId, Quantity quantity, Price orderedMenuPrice, String menuName
    ) {
        this.seq = seq;
        this.order = order;
        this.menuId = menuId;
        this.quantity = quantity;
        this.orderedMenuPrice = orderedMenuPrice;
        this.menuName = menuName;
    }

    public OrderLineItem(Long menuId, Quantity quantity, Price orderedMenuPrice, String menuName) {
        this(null, null, menuId, quantity, orderedMenuPrice, menuName);
    }

    public void associateOrder(Order order) {
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

    public Price getOrderedMenuPrice() {
        return orderedMenuPrice;
    }

    public String getMenuName() {
        return menuName;
    }
}
