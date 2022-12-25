package kitchenpos.order.domain;

import java.util.Objects;
import javax.persistence.Column;
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
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_line_item_orders"), nullable = false)
    private Order order;

    @Column(nullable = false)
    private Long menuId;

    @Column(nullable = false)
    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Order order, Long menuId, long quantity) {
        validation(order, menuId, quantity);
        this.order = order;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    private void validation(Order order, Long menu, long quantity) {
        if (Objects.isNull(order)) {
            throw new IllegalArgumentException("주문 내역이 없습니다.");
        }
        if (Objects.isNull(menu)) {
            throw new IllegalArgumentException("주문 메뉴가 없습니다.");
        }
        if (quantity < 1) {
            throw new IllegalArgumentException("주문 수량이 없습니다.");
        }
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

}
