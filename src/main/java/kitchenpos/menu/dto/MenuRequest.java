package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import java.math.BigDecimal;
import java.util.List;

public class MenuRequest {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    public void setId(final Long id) {
        this.id = id;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    public void setMenuGroupId(final Long menuGroupId) {
        this.menuGroupId = menuGroupId;
    }

    public void setMenuProducts(final List<MenuProductRequest> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public Menu toEntity() {
        Menu menu = new Menu(id, name, price, menuGroupId);

        for (MenuProductRequest menuProductRequest : menuProducts) {
            menuProductRequest.toEntity(menu);
        }

        return menu;
    }

}
