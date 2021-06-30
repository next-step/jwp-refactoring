package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MenuCreate {
    private String name;
    private Price price;
    private Long menuGroupId;
    private List<MenuProductCreate> menuProducts;

    public MenuCreate(String name, Price price, Long menuGroupId, List<MenuProductCreate> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductCreate> getMenuProducts() {
        return menuProducts;
    }

    public List<Long> getProductsIdInMenuProducts() {
        return menuProducts.stream()
                .map(item -> item.getProductId())
                .collect(Collectors.toList());
    }
}
