package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MenuRequest {
    @NotNull
    public String name;
    @NotNull
    @Min(0)
    public BigDecimal price;
    @NotNull
    public Long menuGroupId;
    @NotNull
    public List<MenuProductRequest> menuProducts;

    public MenuRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuRequest of(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        return new MenuRequest(name, price, menuGroupId, menuProducts);
    }

    public Menu toEntity() {
        return Menu.of(name, price, menuGroupId, menuProductsToEntities());
    }

    private List<MenuProduct> menuProductsToEntities() {
        return menuProducts.stream()
                .map(MenuProductRequest::toEntity)
                .collect(Collectors.toList());
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }
}
