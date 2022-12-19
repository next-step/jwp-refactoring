package kitchenpos.order.domain;

import kitchenpos.common.Quantity;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderLineItem {
    public static final String MENU_NULL_EXCEPTION_MESSAGE = "메뉴가 없을 수 없습니다.";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_line_item_orders"), nullable = false)
    private Orders order;
    private Long menuId;
    private Quantity quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Orders order, Long menuId, Quantity quantity) {
        validate(menuId, quantity);
        this.order = order;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setOrder(Orders order) {
        this.order = order;
    }

    public Quantity getQuantity() {
        return this.quantity;
    }

    public Orders getOrder() {
        return this.order;
    }

    private void validate(Long menuId, Quantity quantity) {
        validateNullMenuId(menuId);
        validateNullQuantity(quantity);
    }

    private static void validateNullQuantity(Quantity quantity) {
        if (Objects.isNull(quantity)) {
            throw new IllegalArgumentException();
        }
    }

    private static void validateNullMenuId(Long menuId) {
        if (Objects.isNull(menuId)) {
            throw new IllegalArgumentException(MENU_NULL_EXCEPTION_MESSAGE);
        }
    }
}
