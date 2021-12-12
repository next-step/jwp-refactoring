package kitchenpos.order.domain.orderLineItem;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kitchenpos.menu.domain.menu.Menu;
import kitchenpos.order.domain.order.Order;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderLineItem {

    private static final int MIN_QUANTITY_NUMBER = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Order order, Menu menu, long quantity) {
        validation(order, menu, quantity);
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
    }

    private void validation(Order order, Menu menu, long quantity) {
        if (Objects.isNull(order)) {
            throw new IllegalArgumentException("주문 내역이 없습니다.");
        }
        if (Objects.isNull(menu)) {
            throw new IllegalArgumentException("주문 메뉴가 없습니다.");
        }
        if (quantity < MIN_QUANTITY_NUMBER) {
            throw new IllegalArgumentException("주문 수량이 없습니다.");
        }
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public Menu getMenu() {
        return menu;
    }

    public long getQuantity() {
        return quantity;
    }
}
