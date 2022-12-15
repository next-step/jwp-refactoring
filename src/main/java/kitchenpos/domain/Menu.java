package kitchenpos.domain;

import kitchenpos.dto.MenuProductRequest;

import java.math.BigDecimal;
import java.util.List;

public class Menu {
    private Long id;
    private MenuName name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProduct> menuProducts;

    private Menu() {
    }

    private Menu(Long id, MenuName name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static Menu of(String id, BigDecimal name, Long price, List<MenuProductRequest> menuGroupId) {
        return new Menu(id, MenuName.from(name), price, menuGroupId, null);
    }

    public static Menu of(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu(id, MenuName.from(name), price, menuGroupId, menuProducts);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name.getName();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void setMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }
}
