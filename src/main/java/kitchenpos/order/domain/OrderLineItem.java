package kitchenpos.order.domain;

import kitchenpos.BaseEntity;

import javax.persistence.*;

@Entity
public class OrderLineItem extends BaseEntity {
    private static final int IS_LESS_THAN_OR_EQUAL_TO_ZERO = 0;
    private static final String ERR_TEXT_QUANTITY_MUST_BE_GREATER_THAN_ZERO = "수량은 0보다 커야 합니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "menu_id")
    private Long menuId;

    private long quantity;

    protected OrderLineItem() {
    }

    protected OrderLineItem(final Long menuId, final long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItem of(final Long menuId, final long quantity) {
        if (quantity <= IS_LESS_THAN_OR_EQUAL_TO_ZERO) {
            throw new IllegalArgumentException(ERR_TEXT_QUANTITY_MUST_BE_GREATER_THAN_ZERO);
        }

        return new OrderLineItem(menuId, quantity);
    }

    public void linkOrderId(final Long orderId) {
        this.orderId = orderId;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
