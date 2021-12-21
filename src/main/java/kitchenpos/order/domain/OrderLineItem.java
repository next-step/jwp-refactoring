package kitchenpos.order.domain;

import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.common.domain.BaseEntity;
import kitchenpos.common.domain.Quantity;
import kitchenpos.exception.InvalidArgumentException;
import kitchenpos.menu.domain.Menu;

@Entity
public class OrderLineItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_line_item_orders"))
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_order_line_item_menu"))
    private Menu menu;

    @Embedded
    private Quantity quantity;

    protected OrderLineItem() {
    }

    private OrderLineItem(Menu menu, Long quantity) {
        this.quantity = Quantity.valueOf(quantity);
        setMenu(menu);
    }

    public static OrderLineItem of(Menu menu, Long quantity) {
        return new OrderLineItem(menu, quantity);
    }

    public void relateOrder(Order order) {
        if (this.order != null) {
            this.order.removeOrderLineItem(this);
        }
        this.order = order;
        order.addOrderLineItem(this);
    }

    public boolean equalsOrderLineItem(OrderLineItem other) {
        return equalsOrder(other.order) && menu.equals(other.menu) && quantity
            .equals(other.quantity);
    }

    public boolean equalsOrder(Order order) {
        if (Objects.isNull(this.order)) {
            return false;
        }
        return this.order.equals(order);
    }

    public void removeOrder() {
        this.order = null;
    }

    private void validateMenu(Menu menu) {
        if (Objects.isNull(menu)) {
            throw new InvalidArgumentException("메뉴는 필수 입니다.");
        }
    }

    public Long getSeq() {
        return seq;
    }

    public long getQuantity() {
        return quantity.getQuantity();
    }

    public Menu getMenu() {
        return menu;
    }

    private void setMenu(Menu menu) {
        validateMenu(menu);
        this.menu = menu;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderLineItem that = (OrderLineItem) o;
        return seq.equals(that.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }
}
