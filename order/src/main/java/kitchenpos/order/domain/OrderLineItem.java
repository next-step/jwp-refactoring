package kitchenpos.order.domain;

import org.springframework.util.Assert;

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
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_line_item_order"))
    private Order order;

    @Column
    private Long menuId;

    @Embedded
    private Quantity quantity;

    protected OrderLineItem() {
    }

    private OrderLineItem(Long seq, Order order, Long menuId, Quantity quantity) {
        Assert.notNull(menuId, "메뉴 ID는 비어있을 수 없습니다.");
        Assert.notNull(quantity, "수량은 비어있을 수 없습니다.");

        this.seq = seq;
        this.order = order;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItem of(Long seq, Long menuId, Long quantity) {
        return new OrderLineItem(seq, null, menuId, Quantity.of(quantity));
    }

    public static OrderLineItem of(Long menuId, Long quantity) {
        return new OrderLineItem(null, null, menuId, Quantity.of(quantity));
    }

    public boolean isEqualMenuId(Long menuId) {
        return this.menuId.equals(menuId);
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
        return menuId;
    }

    public Long getQuantity() {
        return quantity.getQuantity();
    }
}
