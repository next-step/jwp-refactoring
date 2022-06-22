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
public class OrderLineItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    private Long menuId;

    private long quantity;

    protected OrderLineItemEntity() {
    }

    private OrderLineItemEntity(Long id, OrderEntity order, Long menuId, long quantity) {
        this.id = id;
        this.order = order;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemEntity of(Long id, OrderEntity order, Long menuId, long quantity) {
        return new OrderLineItemEntity(id, order, menuId, quantity);
    }

    public void mapIntoOrder(OrderEntity order) {
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

    public OrderEntity getOrder() {
        return order;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
