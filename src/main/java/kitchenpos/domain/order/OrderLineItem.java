package kitchenpos.domain.order;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kitchenpos.vo.MenuId;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders order;

    @Enumerated
    private MenuId menuId;

    private long quantity;

    protected OrderLineItem() {
    }

    private OrderLineItem(MenuId menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItem of(MenuId menuId, long quantity) {
        return new OrderLineItem(menuId, quantity);
    }

    public Long getSeq() {
        return this.seq;
    }

    public Orders getOrder() {
        return this.order;
    }
    public MenuId getMenuId() {
        return this.menuId;
    }

    public long getQuantity() {
        return this.quantity;
    }

    public void acceptOrder(Orders order) {
        if (this.order != null) {
            this.order.getOrderLineItems().remove(this);
        }

        this.order = order;
        this.order.getOrderLineItems().add(this);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof OrderLineItem)) {
            return false;
        }
        OrderLineItem orderLineItem = (OrderLineItem) o;
        return Objects.equals(seq, orderLineItem.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }
}
