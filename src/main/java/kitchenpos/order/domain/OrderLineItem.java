package kitchenpos.order.domain;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kitchenpos.menu.domain.Menu;

@Entity
public class OrderLineItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    
    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;
    
    private long quantity;
    
    protected OrderLineItem() {
    }
    
    private OrderLineItem(Order order, Menu menu, long quantity) {
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
    }
    
    public static OrderLineItem of(Order order, Menu menu, long quantity) {
        return new OrderLineItem(order, menu, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public Menu getMenu() {
        return menu;
    }

    public long getQuantity() {
        return quantity;
    }

    public void updateOrder(Order order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderLineItem that = (OrderLineItem) o;
        return quantity == that.quantity && Objects.equals(order, that.order) && Objects.equals(menu, that.menu);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order, menu, quantity);
    }
}
