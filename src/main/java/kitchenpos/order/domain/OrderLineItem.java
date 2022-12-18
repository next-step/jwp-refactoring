package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.common.domain.Quantity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "order_line_item")
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq", nullable = false, columnDefinition = "bigint(20)")
    private Long seq;
    @Column(name = "order_id", nullable = false, columnDefinition = "bigint(20)")
    private Long orderId;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "menu_id", nullable = false, columnDefinition = "bigint(20)", foreignKey = @ForeignKey(name = "fk_order_line_item_menu"))
    private Menu menu;
    @Embedded
    private Quantity quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Menu menu, Long quantity) {
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

    public Long getQuantity() {
        return quantity.value();
    }

    public void addedBy(Long orderId) {
        this.orderId = orderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderLineItem)) return false;
        OrderLineItem that = (OrderLineItem) o;
        return Objects.equals(getOrderId(), that.getOrderId()) && Objects.equals(getMenu(), that.getMenu()) && Objects.equals(getQuantity(), that.getQuantity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrderId(), getMenu(), getQuantity());
    }
}
