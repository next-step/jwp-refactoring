package kitchenpos.domain.order;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import kitchenpos.domain.Quantity;
import kitchenpos.domain.menu.Menu;

@Entity
@Table(name = "order_line_item")
public class OrderLineItem {
    private static final String NOT_EXIST_MENU = "Menu 가 존재하지 않습니다.";
    private static final String NOT_EXIST_ORDER = "Order 가 존재하지 않습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_line_item_orders"), nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_order_line_item_menu"), nullable = false)
    private Menu menu;

    @Embedded
    private Quantity quantity;

    protected OrderLineItem() {}

    private OrderLineItem(Long seq, Order order, Menu menu, long quantity) {
        this(menu, quantity);
        this.seq = seq;
        this.order = order;
    }

    private OrderLineItem(Menu menu, Long quantity) {
        this.menu = menu;
        this.quantity = Quantity.from(quantity);
    }

    public static OrderLineItem of(Long seq, Order order, Menu menu, long quantity) {
        validateExistOrders(order);
        validateExistMenu(menu);
        return new OrderLineItem(seq, order, menu, quantity);
    }

    public static OrderLineItem of(Menu menu, Long quantity) {
        validateExistMenu(menu);
        return new OrderLineItem(menu, quantity);
    }

    public void assignOrders(Order order) {
        this.order = order;
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrders() {
        return order;
    }

    public Menu getMenu() {
        return menu;
    }

    public Long getMenuId() {
        return menu.getId();
    }

    public Quantity getQuantity() {
        return quantity;
    }

    private static void validateExistMenu(Menu menu) {
        if (Objects.isNull(menu)) {
            throw new IllegalArgumentException(NOT_EXIST_MENU);
        }
    }

    private static void validateExistOrders(Order order) {
        if (Objects.isNull(order)) {
            throw new IllegalArgumentException(NOT_EXIST_ORDER);
        }
    }
}
