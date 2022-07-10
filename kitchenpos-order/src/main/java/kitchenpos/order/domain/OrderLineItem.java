package kitchenpos.order.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(nullable = false)
    private Long menuId;

    @Embedded
    private Name menuName;

    @Embedded
    private Price menuPrice;

    private long quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(Long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem(Order order, Long menuId, long quantity) {
        this.order = order;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem(Long menuId, String menuName, BigDecimal menuPrice, long quantity) {
        this(null, menuId, menuName, menuPrice, quantity);
    }

    public OrderLineItem(Order order, Long menuId, String menuName,
        BigDecimal menuPrice, long quantity) {
        this(order, menuId, new Name(menuName), new Price(menuPrice), quantity);
    }

    public OrderLineItem(Order order, Long menuId, Name menuName,
        Price menuPrice, long quantity) {
        this.order = order;
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.quantity = quantity;
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

    public String getMenuName() {
        return menuName.getName();
    }

    public BigDecimal getMenuPrice() {
        return menuPrice.getValue();
    }

    public long getQuantity() {
        return quantity;
    }

    public void updateOrder(Order order) {
        this.order = order;
    }
}
