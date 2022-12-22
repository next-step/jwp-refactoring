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
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "menuId", column = @Column(name = "menu_id")),
            @AttributeOverride(name = "name.name", column = @Column(name = "menu_name")),
            @AttributeOverride(name = "price.price", column = @Column(name = "menu_price"))
    })
    private OrderMenu orderMenu;
    private Quantity quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Orders order, OrderMenu orderMenu, Quantity quantity) {
        validate(orderMenu, quantity);
        this.order = order;
        this.orderMenu = orderMenu;
        this.quantity = quantity;
    }

    private static void validateNullQuantity(Quantity quantity) {
        if (Objects.isNull(quantity)) {
            throw new IllegalArgumentException();
        }
    }

    private static void validateNullOrderMenu(OrderMenu orderMenu) {
        if (Objects.isNull(orderMenu)) {
            throw new IllegalArgumentException(MENU_NULL_EXCEPTION_MESSAGE);
        }
    }

    public Quantity getQuantity() {
        return this.quantity;
    }

    public Orders getOrder() {
        return this.order;
    }

    public void setOrder(Orders order) {
        this.order = order;
    }

    private void validate(OrderMenu orderMenu, Quantity quantity) {
        validateNullOrderMenu(orderMenu);
        validateNullQuantity(quantity);
    }

    public OrderMenu getMenu() {
        return this.orderMenu;
    }
}
