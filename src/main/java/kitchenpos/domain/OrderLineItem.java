package kitchenpos.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "order_line_item")
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    private Long orderId;
    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;
    private Quantity quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Long orderId, Menu menu, long quantity) {
        this.orderId = orderId;
        this.menu = menu;
        this.quantity = new Quantity(quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Menu getMenu() {
        return menu;
    }

    public long getQuantity() {
        return quantity.getValue();
    }
}
