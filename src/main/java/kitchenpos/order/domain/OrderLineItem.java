package kitchenpos.order.domain;

import kitchenpos.common.domain.Price;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Table(name = "order_line_item")
@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Embedded
    private OrderMenu orderMenu = new OrderMenu();

    private long quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(Long menuId, String name, Price price, long quantity) {
        this.orderMenu = new OrderMenu(menuId, name ,price);
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return orderMenu.getMenuId();
    }

    public long getQuantity() {
        return quantity;
    }

    public void updateOrder(Order order) {
        this.order = order;
    }
}
