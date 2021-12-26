package kitchenpos.domain;

import javax.persistence.*;

@Entity
@SequenceGenerator(
        name = "ORDER_LINE_ITEM_SEQ_GENERATOR",
        sequenceName = "ORDER_LINE_ITEM_SEQ",
        initialValue = 1,
        allocationSize = 1)
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORDER_LINE_ITEM_SEQ_GENERATOR")
    private Long seq;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order orderId;
    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menuId;
    private long quantity;

    public OrderLineItem() {
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(final Long seq) {
        this.seq = seq;
    }

    public Long getOrderId() {
        return orderId.getId();
    }

    public void setOrderId(final Long orderId) {
        this.orderId.setId(orderId);
    }

    public Long getMenuId() {
        return menuId.getId();
    }

    public void setMenuId(final Long menuId) {
        this.menuId.setId(menuId);
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }
}
