package kitchenpos.domain.order;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import kitchenpos.domain.menu.Menu;

@Entity
@Table(name = "order_line_item")
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_line_item_orders"), nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_order_line_item_menu"), nullable = false)
    private Menu menu;

    @Column(name = "quantity")
    private Long quantity;

    public static OrderLineItem of(Menu menu, Long quantity) {
        return new OrderLineItem(null, menu, quantity);
    }

    public OrderLineItem() {
    }

    public OrderLineItem(Long seq, Menu menu, Long quantity) {
        this.seq = seq;
        this.menu = menu;
        this.quantity = quantity;
    }

    public void orderedBy(Order order) {
        this.order = order;
    }

    @Deprecated
    public void setOrderId(final Long orderId) {
        this.order.setId(orderId);
    }

    public Long getMenuId() {
        return this.menu.getId();
    }

    @Deprecated
    public void setQuantity(final long quantity) {
        this.quantity = quantity;
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

    public Long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        if (Objects.isNull(seq)) {
            return false;
        }

        OrderLineItem that = (OrderLineItem) o;
        return Objects.equals(seq, that.seq);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
