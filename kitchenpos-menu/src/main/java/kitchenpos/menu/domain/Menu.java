package kitchenpos.menu.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Embedded
    private MenuPrice price;
    @ManyToOne(fetch = FetchType.LAZY)
    private MenuGroup menuGroup;
    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {
    }

    private Menu(MenuBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.price = MenuPrice.of(builder.price);
        this.menuGroup = builder.menuGroup;
        this.menuProducts = builder.menuProducts;
    }

    public Long getMenuGroupId() {
        return menuGroup.getId();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.value();
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getMenuProducts();
    }

    public void addMenuProducts(MenuProducts menuProducts) {
        this.menuProducts.addMenuProducts(menuProducts);
    }

    public static MenuBuilder builder() {
        return new MenuBuilder();
    }

    public static class MenuBuilder {
        private Long id;
        private String name;
        private BigDecimal price;
        private MenuGroup menuGroup;
        private MenuProducts menuProducts = new MenuProducts();

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

        public MenuBuilder menuGroup(MenuGroup menuGroup) {
            this.menuGroup = menuGroup;
            return this;
        }

        public MenuBuilder menuProducts(MenuProducts menuProducts) {
            this.menuProducts.addMenuProducts(menuProducts);
            return this;
        }

        public Menu build() {
            return new Menu(this);
        }
    }
}
