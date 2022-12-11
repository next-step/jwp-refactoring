package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;

public class Menu {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProduct> menuProducts;

    public Menu(){}

    private Menu(MenuBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.price = builder.price;
        this.menuGroupId = builder.menuGroupId;
        this.menuProducts = builder.menuProducts;
    }

    public static MenuBuilder builder() {
        return new MenuBuilder();
    }

    public static class MenuBuilder {
        private Long id;
        private String name;
        private BigDecimal price;
        private Long menuGroupId;
        private List<MenuProduct> menuProducts;

        public MenuBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public MenuBuilder name(String name) {
            this.name = name;
            return this;
        }

        public MenuBuilder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public MenuBuilder menuGroupId(Long menuGroupId) {
            this.menuGroupId = menuGroupId;
            return this;
        }

        public MenuBuilder menuProducts(List<MenuProduct> menuProducts) {
            this.menuProducts = menuProducts;
            return this;
        }

        public Menu build() {
            return new Menu(this);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public void setMenuGroupId(final Long menuGroupId) {
        this.menuGroupId = menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void setMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }
}
