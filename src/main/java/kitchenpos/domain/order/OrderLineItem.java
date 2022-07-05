package kitchenpos.domain.order;

import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import kitchenpos.domain.menu.Menu;

public class OrderLineItem {

    private Long seq;
    private Long orderId;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "order_id",
        foreignKey = @ForeignKey(name = "FK_ORDER_LINE_ITEM_TO_ORDER"),
        nullable = false
    )
    private Order order;
    private Long menuId;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "menu_id",
        foreignKey = @ForeignKey(name = "FK_ORDER_LINE_ITEM_TO_MENU"),
        nullable = false
    )
    private Menu menu;

    private long quantity;

    public OrderLineItem() {

    }

    public OrderLineItem(Menu menu, long quantity) {
        this.menu = menu;
        this.quantity = quantity;
    }

    public OrderLineItem(Order order, Menu menu, long quantity) {
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(final Long seq) {
        this.seq = seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(final Long orderId) {
        this.orderId = orderId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(final Long menuId) {
        this.menuId = menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }
}
