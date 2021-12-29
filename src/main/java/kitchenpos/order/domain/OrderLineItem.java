package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;

import javax.persistence.*;
import java.math.BigDecimal;

@Table(name = "order_line_item")
@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "menu_id", nullable = false)
    private Long menuId;

    @Column(name = "menu_name", nullable = false)
    private String menuName;

    @Embedded
    private OrderLineItemPrice menuPrice;

    @Embedded
    private OrderQuantity orderQuantity;

    protected OrderLineItem() {

    }

    private OrderLineItem(Long menuId, String menuName, BigDecimal menuPrice, long quantity) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuPrice = new OrderLineItemPrice(menuPrice);
        this.orderQuantity = new OrderQuantity(quantity);
    }

    public static OrderLineItem create(Long menuId, String menuName, BigDecimal menuPrice, long quantity) {
        return new OrderLineItem(menuId, menuName, menuPrice, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public BigDecimal getMenuPrice() {
        return menuPrice.getMenuPrice();
    }

    public long getQuantity() {
        return orderQuantity.getQuantity();
    }

    private boolean matchOrderItem(Long menuId) {
        return this.menuId.equals(menuId);
    }

    public boolean notMatchOrderItemName(Long menuId, String menuName) {
        return matchOrderItem(menuId) && !this.menuName.equals(menuName);
    }

    public boolean notMatchOrderItemPrice(Long menuId, BigDecimal menuPrice) {
        return matchOrderItem(menuId) && this.menuPrice.compareTo(menuPrice);
    }
}
