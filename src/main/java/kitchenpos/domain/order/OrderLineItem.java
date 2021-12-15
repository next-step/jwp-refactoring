package kitchenpos.domain.order;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
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
    private static final String NOT_EXIST_ORDERS = "Orders 가 존재하지 않습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_line_item_orders"), nullable = false)
    private Orders orders;

    @ManyToOne
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_order_line_item_menu"), nullable = false)
    private Menu menu;

    @Embedded
    private Quantity quantity;

    protected OrderLineItem() {}

    private OrderLineItem(Long seq, Orders orders, Menu menu, long quantity) {
        this(menu, quantity);
        this.seq = seq;
        this.orders = orders;
    }

    private OrderLineItem(Menu menu, Long quantity) {
        this.menu = menu;
        this.quantity = Quantity.from(quantity);
    }

    public static OrderLineItem of(Long seq, Orders orders, Menu menu, long quantity) {
        validateExistOrders(orders);
        validateExistMenu(menu);
        return new OrderLineItem(seq, orders, menu, quantity);
    }

    public static OrderLineItem of(Menu menu, Long quantity) {
        validateExistMenu(menu);
        return new OrderLineItem(menu, quantity);
    }

    public void assignOrders(Orders orders) {
        this.orders = orders;
    }

    public Long getSeq() {
        return seq;
    }

    public Orders getOrders() {
        return orders;
    }

    public Menu getMenu() {
        return menu;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    private static void validateExistMenu(Menu menu) {
        if (Objects.isNull(menu)) {
            throw new IllegalArgumentException(NOT_EXIST_MENU);
        }
    }

    private static void validateExistOrders(Orders orders) {
        if (Objects.isNull(orders)) {
            throw new IllegalArgumentException(NOT_EXIST_ORDERS);
        }
    }
}
