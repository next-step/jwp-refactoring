package kitchenpos.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;
    @Column(name = "menu_name", nullable = false)
    private String menuName;
    @Column(name = "menu_price", nullable = false)
    private BigDecimal menuPrice;
    @Column(name = "menu_id", nullable = false)
    private Long menuId;
    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(final Long seq, final Order orderId, final String menuName, final BigDecimal menuPrice,
                         final Long menuId, final long quantity) {
        this.seq = seq;
        this.order = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
    }

    private OrderLineItem(final Menu menu, final long quantity) {
        this(null, null, menu.getName(), menu.getPrice(), menu.getId(), quantity);
    }

    public static OrderLineItem of(final Menu menu, final long quantity) {
        return new OrderLineItem(menu, quantity);
    }

    public void updateOrder(final Order order) {
        this.order = order;
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public String getMenuName() {
        return menuName;
    }

    public BigDecimal getMenuPrice() {
        return menuPrice;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "OrderLineItem{" +
                "seq=" + seq +
                ", orderId=" + order.getId() +
                ", menuName='" + menuName + '\'' +
                ", menuPrice=" + menuPrice +
                ", menuId=" + menuId +
                ", quantity=" + quantity +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OrderLineItem that = (OrderLineItem) o;
        return quantity == that.quantity && Objects.equals(seq, that.seq) && Objects.equals(order, that.order) && Objects.equals(menuName, that.menuName) && Objects.equals(menuPrice, that.menuPrice) && Objects.equals(menuId, that.menuId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, order, menuName, menuPrice, menuId, quantity);
    }
}

