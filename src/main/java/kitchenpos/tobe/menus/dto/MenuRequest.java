package kitchenpos.tobe.menus.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.tobe.common.domain.Name;
import kitchenpos.tobe.common.domain.Price;
import kitchenpos.tobe.common.domain.Validator;
import kitchenpos.tobe.menus.domain.Menu;
import kitchenpos.tobe.menus.domain.MenuProducts;

public class MenuRequest {

    private final String name;

    private final BigDecimal price;

    private final List<MenuProductRequest> menuProducts;

    private final Long menuGroupId;

    public MenuRequest(
        final String name,
        final BigDecimal price,
        final List<MenuProductRequest> menuProducts,
        final Long menuGroupId
    ) {
        this.name = name;
        this.price = price;
        this.menuProducts = menuProducts;
        this.menuGroupId = menuGroupId;
    }

    public Menu toMenu(final Validator<Menu> validator) {
        return new Menu(
            new Name(name),
            new Price(price),
            new MenuProducts(
                menuProducts
                    .stream()
                    .map(MenuProductRequest::toMenuProduct)
                    .collect(Collectors.toList())
            ),
            menuGroupId,
            validator
        );
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }
}
