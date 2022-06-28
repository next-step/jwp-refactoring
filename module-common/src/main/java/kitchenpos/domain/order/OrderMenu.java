package kitchenpos.domain.order;

import static java.util.Objects.requireNonNull;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import kitchenpos.domain.common.Name;
import kitchenpos.domain.common.Price;
import kitchenpos.domain.menu.Menu;

@Embeddable
public class OrderMenu {

    @Column(name = "menu_id", nullable = false)
    private Long menuId;
    @Embedded
    private Name menuName;
    @Embedded
    private Price menuPrice;

    protected OrderMenu() {
    }

    public OrderMenu(Long menuId, Name menuName, Price menuPrice) {
        this.menuId = requireNonNull(menuId, "주문한 메뉴가 없습니다.");
        this.menuName = requireNonNull(menuName, "주문한 메뉴의 이름이 없습니다.");
        this.menuPrice = requireNonNull(menuPrice, "주문한 메뉴의 가격이 없습니다.");
    }

    public OrderMenu(Long menuId, String menuName, BigDecimal menuPrice) {
        this.menuId = requireNonNull(menuId, "주문한 메뉴가 없습니다.");
        this.menuName = new Name(menuName);
        this.menuPrice = new Price(menuPrice);
    }

    public static OrderMenu of(Menu menu) {
        return new OrderMenu(menu.getId(), new Name(menu.getName()), new Price(menu.getPrice()));
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderMenu orderMenu = (OrderMenu) o;
        return Objects.equals(menuId, orderMenu.menuId) && Objects.equals(menuName, orderMenu.menuName)
            && Objects.equals(menuPrice, orderMenu.menuPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuId, menuName, menuPrice);
    }

    @Override
    public String toString() {
        return "OrderMenu{" +
            "menuId=" + menuId +
            ", menuName=" + menuName +
            ", menuPrice=" + menuPrice +
            '}';
    }

}
