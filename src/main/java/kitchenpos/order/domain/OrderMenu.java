package kitchenpos.order.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.common.domain.MustHaveName;
import kitchenpos.common.domain.Price;
import kitchenpos.exception.InvalidArgumentException;

@Embeddable
public class OrderMenu {

    private Long menuId;

    @AttributeOverride(name = "name", column = @Column(name = "menu_name", nullable = false))
    private MustHaveName menuName;

    @AttributeOverride(name = "price", column = @Column(name = "menu_price", nullable = false))
    private Price menuPrice;

    protected OrderMenu() {
    }

    public OrderMenu(Long menuId, String menuName, BigDecimal menuPrice) {
        validateMenu(menuId, menuName, menuPrice);
        this.menuId = menuId;
        this.menuName = MustHaveName.valueOf(menuName);
        this.menuPrice = Price.valueOf(menuPrice);
    }

    public static OrderMenu of(Long menuId, String menuName, BigDecimal menuPrice) {
        return new OrderMenu(menuId, menuName, menuPrice);
    }


    public Long getMenuId() {
        return menuId;
    }

    public String getMenuName() {
        return menuName.toString();
    }

    public BigDecimal getMenuPrice() {
        return menuPrice.toBigDecimal();
    }

    private void validateMenu(Long menuId, String menuName, BigDecimal menuPrice) {
        if (Objects.isNull(menuId) || Objects.isNull(menuName) || Objects.isNull(menuPrice)) {
            throw new InvalidArgumentException("메뉴는 필수 입니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderMenu orderMenu = (OrderMenu) o;
        return menuId.equals(orderMenu.menuId) && menuName.equals(orderMenu.menuName) && menuPrice
            .equals(orderMenu.menuPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuId, menuName, menuPrice);
    }

}
