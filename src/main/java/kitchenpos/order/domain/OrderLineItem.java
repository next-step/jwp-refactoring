package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
    @JoinColumn(name = "order_id")
    private Order order;

    @Embedded
    private OrderMenu orderMenu;

    @Column(nullable = false)
    private long quantity;

    protected OrderLineItem() {}

    private OrderLineItem(Long menuId, String menuName, long menuPrice, long quantity) {
        this.orderMenu = OrderMenu.createOrderMenu(menuId, menuName, menuPrice);
        this.quantity = quantity;
    }

    public static OrderLineItem createOrderLineItem(Long menuId, String menuName, long menuPrice, long quantity) {
        return new OrderLineItem(menuId, menuName, menuPrice, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return order.getId();
    }

    public Long getMenuId() {
        return orderMenu.getMenuId();
    }

    public String getMenuName() {
        return orderMenu.getMenuName();
    }

    public Long getMenuPrice() {
        return orderMenu.getMenuPrice()
                .getPriceLongValue();
    }

    public long getQuantity() {
        return quantity;
    }

    public void addOrder(Order order) {
        this.order = order;
    }
}
