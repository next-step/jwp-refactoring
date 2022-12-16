package kitchenpos.order.domain;

import kitchenpos.common.domain.Quantity;
import kitchenpos.common.exception.InvalidParameterException;

import javax.persistence.*;

@Entity
public class OrderLineItem {
    private static final String ERROR_MESSAGE_ORDER_LINE_ITEM_MENU_IS_NULL = "메뉴는 필수입니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    @Embedded
    private OrderMenu menu;
    @Embedded
    private Quantity quantity;

    protected OrderLineItem() {}

    private OrderLineItem(Long seq, Order order, OrderMenu menu, long quantity) {
        validate(menu);
        this.seq = seq;
        this.order = order;
        this.menu = menu;
        this.quantity = Quantity.from(quantity);
    }

    private void validate(OrderMenu menu) {
        if (menu == null) {
            throw new InvalidParameterException(ERROR_MESSAGE_ORDER_LINE_ITEM_MENU_IS_NULL);
        }
    }

    private OrderLineItem(OrderMenu menu, long quantity) {
        this(null, null, menu, quantity);
    }

    public static OrderLineItem of(OrderMenu menu, long quantity) {
        return new OrderLineItem(menu, quantity);
    }

    public static OrderLineItem of(Long seq, Order order, OrderMenu menu, long quantity) {
        return new OrderLineItem(seq, order, menu, quantity);
    }

    public void updateOrder(Order order) {
        this.order = order;
    }

    public Long seq() {
        return seq;
    }

    public Order order() {
        return order;
    }

    public OrderMenu menu() {
        return menu;
    }

    public Quantity quantity() {
        return quantity;
    }

    public Long menuId() {
        return menu.id();
    }

    public long quantityValue() {
        return quantity.value();
    }
}
