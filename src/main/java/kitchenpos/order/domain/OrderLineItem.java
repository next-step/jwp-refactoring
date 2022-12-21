package kitchenpos.order.domain;

import java.math.BigDecimal;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    private Long menuId;
    private String menuName;
    @Embedded
    private OrderMenuPrice menuPrice;
    private long quantity;

    protected OrderLineItem() {}

    private OrderLineItem(Long seq, Long menuId, String menuName, BigDecimal menuPrice, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuPrice = OrderMenuPrice.from(menuPrice);
        this.quantity = quantity;
    }

    public static OrderLineItem generate(Long menuId, String orderMenuName, BigDecimal orderMenuPrice, long quantity) {
        return new OrderLineItem(null, menuId, orderMenuName, orderMenuPrice, quantity);
    }

    public static OrderLineItem of(Long seq, Long menuId, String orderMenuName, BigDecimal orderMenuPrice, long quantity) {
        return new OrderLineItem(seq, menuId, orderMenuName, orderMenuPrice, quantity);
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
        return menuPrice.value();
    }

    public long getQuantity() {
        return quantity;
    }

}
