package kitchenpos.order.domain;

import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;
import org.springframework.util.Assert;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_line_item_order"))
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_order_line_item_menu"))
    private Menu menu;

    @Embedded
    private Quantity quantity;

    protected OrderLineItem() {
    }

    private OrderLineItem(Long seq, Order order, Menu menu, Quantity quantity) {
        Assert.notNull(menu, "메뉴는 비어있을 수 없습니다.");
        Assert.notNull(quantity, "수량은 비어있을 수 없습니다.");

        this.seq = seq;
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
    }

    public static OrderLineItem of(Long seq, Menu menu, Long quantity) {
        return new OrderLineItem(seq, null, menu, Quantity.of(quantity));
    }

    public static OrderLineItem of(Menu menu, Long quantity) {
        return new OrderLineItem(null, null, menu, Quantity.of(quantity));
    }

    public void updateOrder(Order order) {
        this.order = order;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return order.getId();
    }

    public Long getMenuId() {
        return menu.getId();
    }

    public Long getQuantity() {
        return quantity.getQuantity();
    }
}
