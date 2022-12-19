package kitchenpos.order.domain;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;

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
    @Column(name = "menu_id", nullable = false, columnDefinition = "bigint(20)")
    private Long menuId;
    @AttributeOverride(name = "name", column = @Column(name = "menu_name", nullable = false))
    private Name menuName;
    @AttributeOverride(name = "price", column = @Column(name = "menu_price", nullable = false))
    private Price menuPrice;
    @Embedded
    private Quantity quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Menu menu, Long quantity) {
        this.menuId = menu.getId();
        this.menuName = new Name(menu.getName());
        this.menuPrice = new Price(menu.getPrice());
        this.quantity = new Quantity(quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Name getMenuName() {
        return menuName;
    }

    public Price getMenuPrice() {
        return menuPrice;
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
        return Objects.equals(getOrderId(), that.getOrderId()) && Objects.equals(getMenuId(), that.getMenuId()) && Objects.equals(getMenuName(), that.getMenuName()) && Objects.equals(getMenuPrice(), that.getMenuPrice()) && Objects.equals(getQuantity(), that.getQuantity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrderId(), getMenuId(), getMenuName(), getMenuPrice(), getQuantity());
    }
}
