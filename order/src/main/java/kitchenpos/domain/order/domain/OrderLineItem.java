package kitchenpos.domain.order.domain;

import javax.persistence.*;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private Long menuId;

    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Long menuId, Long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem(Long menuId, int quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public void addMenu(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    public Long getMenuId() {return menuId;}

    public long getQuantity() {
        return quantity;
    }
}
