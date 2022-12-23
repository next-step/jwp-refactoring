package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import kitchenpos.domain.Name;
import kitchenpos.domain.Price;
import kitchenpos.menu.domain.Menu;

@Embeddable
public class OrderMenu {

    @Column(nullable = false)
    private Long menuId;
    @Embedded
    private Name menuName;
    @Embedded
    private Price menuPrice;

    protected OrderMenu() {
    }

    protected OrderMenu(Long menuId, Name name, Price price) {
        this.menuId = menuId;
        this.menuName = name;
        this.menuPrice = price;
    }

    public static OrderMenu from(Menu menu) {
        return new OrderMenu(menu.getId(), menu.getName(), menu.getPrice());
    }

    public Long getMenuId() {
        return menuId;
    }

    public Name getName() {
        return menuName;
    }

    public Price getPrice() {
        return menuPrice;
    }


}
