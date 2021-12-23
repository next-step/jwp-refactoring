package kitchenpos.domain.order;



import kitchenpos.domain.menu.Menu;

import javax.persistence.*;
import java.util.Objects;

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

    public OrderLineItem() {
    }

    public OrderLineItem(Order order, Menu menu, long quantity) {
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
    }

    public OrderLineItem(Order order, Long menuId, long quantity) {
        this.order = order;
        this.menu = new Menu(menuId);
        this.quantity = quantity;
    }

    public OrderLineItem(Menu menu, long quantity) {
        this.menu = menu;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public long getQuantity() {
        return quantity;
    }

    public Order getOrder() {
        return order;
    }

    public Menu getMenu() {
        return menu;
    }

    public void addOrder(Order order) {
        this.order = order;
    }

    public void addMenu(Menu menu) {
        this.menu = menu;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderLineItem that = (OrderLineItem) o;
        return quantity == that.quantity && Objects.equals(seq, that.seq) && Objects.equals(order, that.order) && Objects.equals(menu, that.menu);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, order, menu, quantity);
    }

    @Override
    public String toString() {
        return "OrderLineItem{" +
                "seq=" + seq +
                ", order=" + order.getId() +
                ", menu="  +
                ", quantity=" + quantity +
                '}';
    }
}
