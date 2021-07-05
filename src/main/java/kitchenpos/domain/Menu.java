package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;

public class Menu {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProduct> menuProducts;

    public Menu() {
        // empty
    }

    public Menu(Builder builder) {
        this.name = builder.name;
        this.price = builder.price;
        this.menuGroupId = builder.menuGroupId;
        this.menuProducts = builder.menuProducts;
        ;
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

    public static class Builder {
        private final String name;
        private final BigDecimal price;
        private Long menuGroupId;
        private List<MenuProduct> menuProducts;

        private Builder(final String name, final BigDecimal price) {
            this.name = name;
            this.price = price;
        }

        public static Builder of(final String name, final BigDecimal price) {
            return new Builder(name, price);
        }

        public Builder menuGroupId(final Long menuGroupId) {
            this.menuGroupId = menuGroupId;
            return this;
        }

        public Builder menuProducts(final List<MenuProduct> menuProducts) {
            this.menuProducts = menuProducts;
            return this;
        }

        public Menu build() {
            return new Menu(this);
        }
    }
}
