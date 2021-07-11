package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

@Table(name = "order_line_item")
@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // 양방향
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne // 단방향
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @Column(name = "quantity")
    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(final Menu menu, final long quantity) {
        this.menu = menu;
        this.quantity = quantity;
    }

    public OrderLineItem(final Long id, final Order order, final Menu menu, final long quantity) {
        this.id = id;
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
    }

    public void addedBy(final Order order) {
        this.order = order;
    }

    public Long getId() {
        return id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderLineItem that = (OrderLineItem) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
