package kitchenpos.order.domain;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_line_item_orders"))
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_order_line_item_menu"))
    private Menu menu;

    private long quantity;

    protected OrderLineItem() {
        // empty
    }

    public OrderLineItem(final Menu menu, final long quantity) {
        this.menu = menu;
        this.quantity = quantity;
    }

    public void connectOrder(final Order order) {
        this.order = order;
    }

    public Long getSeq() {
        return seq;
    }

    public long getQuantity() {
        return quantity;
    }

    public Long getOrderId() {
        if (Objects.isNull(this.order)) {
            return 0L;
        }
        return this.order.getId();
    }

    public Long getMenuId() {
        if (Objects.isNull(this.menu)) {
            return 0L;
        }
        return this.menu.getId();
    }
}
