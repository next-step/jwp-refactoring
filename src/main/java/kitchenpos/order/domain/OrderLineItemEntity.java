package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuEntity;

import javax.persistence.*;

@Entity
public class OrderLineItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private MenuEntity menu;

    private long quantity;

    public OrderLineItemEntity() {
    }

    public OrderLineItemEntity(Long seq, OrderEntity order, MenuEntity menu, long quantity) {
        this.seq = seq;
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
    }

    public OrderLineItemEntity(MenuEntity menu, long quantity) {
        this.menu = menu;
        this.quantity = quantity;
    }

    public void updateOrder(OrderEntity orderEntity) {
        this.order = orderEntity;
    }

    public Long getSeq() {
        return seq;
    }

    public OrderEntity getOrder() {
        return order;
    }

    public MenuEntity getMenu() {
        return menu;
    }

    public long getQuantity() {
        return quantity;
    }

    public Long getOrderId() {
        if (order == null){
            return null;
        }
        return order.getId();
    }

    public Long getMenuId() {
        if (menu == null){
            return null;
        }
        return menu.getId();
    }
}
