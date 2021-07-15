package kitchenpos.menu.domain;

import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.common.Price;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Embedded
    private Price price;

    private Long menuGroupId;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {
        // empty
    }

    public Menu(Builder builder) {
        this.name = builder.name;
        this.price = builder.price;
        this.menuGroupId = builder.menuGroupId;
        this.menuProducts.add(builder.menuProducts);
    }

    public Long getId() {
        return id;
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

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }

    public static class Builder {
        private final String name;
        private final Price price;
        private Long menuGroupId;
        private List<MenuProduct> menuProducts;

        private Builder(final String name, final Price price) {
            this.name = name;
            this.price = price;
        }

        public static Builder of(final String name, final Price price) {
            return new Builder(name, price);
        }

        public Builder menuGroup(final Long menuGroupId) {
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
