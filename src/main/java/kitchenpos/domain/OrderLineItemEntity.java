package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    private OrderEntity order;

    @ManyToOne(fetch = FetchType.LAZY)
    private MenuEntity menu;

    private Long quantity;

    protected OrderLineItemEntity() {
    }

    public OrderLineItemEntity(MenuEntity menu, long quantity) {
        this.menu = menu;
        this.quantity = quantity;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public Long getId() {
        return id;
    }

    public OrderEntity getOrder() {
        return order;
    }

    public MenuEntity getMenu() {
        return menu;
    }

    public Long getQuantity() {
        return quantity;
    }
}
