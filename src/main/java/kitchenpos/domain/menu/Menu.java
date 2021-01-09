package kitchenpos.domain.menu;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Menu {
    private final Long id;
    private final String name;
    private final MenuPrice price;
    private final Long menuGroupId;
    private final List<MenuProduct> menuProducts;

    public Menu(final Long id, final String name, final BigDecimal price, final Long menuGroupId,
         final List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = new MenuPrice(price);
        this.menuGroupId = menuGroupId;
        this.menuProducts = new ArrayList<>(menuProducts);
    }

    public Menu(final Long id, final String name, final BigDecimal price, final Long menuGroupId) {
        this(id, name, price, menuGroupId, new ArrayList<>());
    }

    public static Menu of(final String name, final BigDecimal price, final Long menuGroupId, final List<MenuProduct> menuProducts) {
        return new Menu(null, name, price, menuGroupId, menuProducts);
    }

    public static Menu of(final Menu menu, final List<MenuProduct> menuProducts) {
        return new Menu(menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroupId(), menuProducts);
    }

    public static Menu of(final String name, final BigDecimal price, final Long menuGroupId) {
        return new Menu(null, name, price, menuGroupId, new ArrayList<>());
    }

    public boolean isMoreExpensive(final BigDecimal price) {
        return this.getPrice().compareTo(price) > 0;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
