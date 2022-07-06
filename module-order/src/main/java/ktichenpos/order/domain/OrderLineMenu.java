package ktichenpos.order.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class OrderLineMenu {
    @Column(nullable = false)
    private Long menuId;
    @Column(nullable = false)
    private String menuName;
    @Column(nullable = false)
    private BigDecimal menuPrice;

    private OrderLineMenu() {

    }

    public OrderLineMenu(Long menuId, String menuName, BigDecimal menuPrice) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
    }

    public Long getMenuId() {
        return menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public BigDecimal getMenuPrice() {
        return menuPrice;
    }
}
