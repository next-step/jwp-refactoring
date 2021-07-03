package kitchenpos.domain.order;

import javax.persistence.*;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_line_item_orders"))
    private Order order;

    private Long menuId;
    private long quantity;

    public Long getSeq() {
        return seq;
    }

    // for jpa
    public OrderLineItem() {
    }

    public static OrderLineItem of(Order order, Long menuId, long quantity){
        return new OrderLineItem(null, order, menuId, quantity);
    }

    private OrderLineItem(Long seq, Order order, Long menuId, long quantity) {
        this.seq = seq;
        this.order = order;
        this.menuId = menuId;
        this.quantity = quantity;
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

    protected void setOrder(Order order) {
        this.order = order;
    }
}
