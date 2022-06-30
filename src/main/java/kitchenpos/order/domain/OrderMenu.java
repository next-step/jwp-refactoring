package kitchenpos.order.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import kitchenpos.product.domain.Price;

@Embeddable
public class OrderMenu {
    @Column(nullable = false)
    private Long menuId;

    @Column(nullable = false)
    private String menuName;

    @Embedded
    @AttributeOverride(name = "price", column = @Column(name = "menu_price"))
    private Price menuPrice;

    protected OrderMenu() {
    }

    private OrderMenu(Long menuId, String menuName, long menuPrice) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuPrice = Price.from(menuPrice);
    }

    public static OrderMenu createOrderMenu(Long menuId, String menuName, long menuPrice) {
        return new OrderMenu(menuId, menuName, menuPrice);
    }

    public Long getMenuId() {
        return menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public Price getMenuPrice() {
        return menuPrice;
    }
}
