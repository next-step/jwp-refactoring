package kitchenpos.order.domain;

import kitchenpos.product.domain.Price;

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

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_line_item_orders"))
    private Order order;

    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_order_line_item_menu"))
    private Long menuId;

    @Column(nullable = false)
    private String menuName;

    @Embedded
    private Price orderedPrice;

    private long quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(Long menuId, Integer quantity) {
        this(null, null, menuId, null, null, quantity);
    }

    public OrderLineItem(Long menuId, String name, Price price, long quantity) {
        this(null, null, menuId, name, price, quantity);
    }

    public OrderLineItem(Long seq, Order order, Long menuId, String menuName, Price orderedPrice, long quantity) {
        this.seq = seq;
        this.order = order;
        this.menuId = menuId;
        this.menuName = menuName;
        this.orderedPrice = orderedPrice;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(final Long seq) {
        this.seq = seq;
    }

    public Order getOrder() {
        return order;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getMenuName() {
        return menuName;
    }

    public Price getOrderedPrice() {
        return orderedPrice;
    }
}
