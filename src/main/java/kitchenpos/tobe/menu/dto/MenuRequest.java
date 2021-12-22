package kitchenpos.tobe.menu.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.tobe.common.domain.Name;
import kitchenpos.tobe.common.domain.Price;
import kitchenpos.tobe.menu.domain.Menu;
import kitchenpos.tobe.menu.domain.MenuProducts;
import kitchenpos.tobe.menu.domain.MenuValidator;

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

    public Menu toMenu(final MenuValidator menuValidator) {
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
            menuValidator
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
