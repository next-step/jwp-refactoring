package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;

import javax.persistence.*;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER)
    private Menu menu;

    private long quantity;

    protected OrderLineItem() {}

    public OrderLineItem(Long seq, Long orderId, Long menuId, long quantity) {
        this.id = seq;
        this.quantity = quantity;
    }

    public static OrderLineItem of(Long seq, Long orderId, Long menuId, long quantity) {
        return new OrderLineItem(seq, orderId, menuId, quantity);
    }

    public void setOrderId(final Long orderId) {
    }

    public Long getMenuId() {
        return null;
    }

}
