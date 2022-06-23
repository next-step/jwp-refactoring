package kitchenpos.order.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "order_line_item")
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private Long menuId;

    @Column(nullable = false)
    private long quantity;

    protected OrderLineItem() {
    }

    private OrderLineItem(Long id, Order order, Long menuId, long quantity) {
        this.id = id;
        this.order = order;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItem of(Long id, Order order, Long menuId, long quantity) {
        return new OrderLineItem(id, order, menuId, quantity);
    }

    public void mapIntoOrder(Order order) {
        this.order = order;
        order.addOrderLineItem(this);
    }

    public void validateMenu() {
        if (Objects.isNull(menuId)) {
            throw new IllegalArgumentException();
        }
    }

    public Long getId() {
        return id;
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
}
