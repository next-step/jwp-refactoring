package kitchenpos.domain.order;

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

@Entity
@Table(name = "order_line_item")
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long seq;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_line_item_orders"), nullable = false)
    private Order order;
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_order_line_item_menu"), nullable = false)
    private Long menuId;
    @Embedded
    private Quantity quantity;

    protected OrderLineItem() {}

    private OrderLineItem(Long menuId, Long quantity) {
        this.menuId = menuId;
        this.quantity = Quantity.from(quantity);
    }

    public static OrderLineItem of(Long menuId, Long quantity) {
        return new OrderLineItem(menuId, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return this.order.getId();
    }

    public Long getMenuId() {
        return menuId;
    }

    public long findQuantity() {
        return quantity.getValue();
    }

    public void mappedByOrder(Order order) {
        this.order = order;
    }
}
