package kitchenpos.menu.dto;

import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MenuCreateRequest {
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts = new ArrayList<>();

    public MenuCreateRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts.addAll(menuProducts);
    }

    public Menu of(MenuGroup menuGroup) {
        return new Menu(name, new Price(price), menuGroup);
    }

    public MenuProducts convertMenuProducts(Function<MenuProductRequest, MenuProduct> converter) {
        List<MenuProduct> menuProducts = this.menuProducts.stream()
                .map(converter)
                .collect(Collectors.toList());

        return new MenuProducts(menuProducts);
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
