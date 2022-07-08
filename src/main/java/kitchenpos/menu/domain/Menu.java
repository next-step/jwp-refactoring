package kitchenpos.menu.domain;

import kitchenpos.menu.exception.MenuException;
import kitchenpos.menu.exception.MenuExceptionType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Embedded
    private MenuPrice price;
    @ManyToOne(fetch = FetchType.LAZY)
    private MenuGroup menuGroup;
    @Embedded
    private MenuProducts menuProducts;

    protected Menu() {
    }

    public Menu(final Long id, final String name, final BigDecimal price, final  MenuGroup menuGroup) {
        this.id = id;
        this.name = name;
        this.price = MenuPrice.of(price);
        this.menuGroup = menuGroup;

    }

    public static Menu of(final String name, final BigDecimal price, final MenuGroup menuGroup) {
        return new Menu(null, name, price, menuGroup);
    }

    public void addMenuProducts(final List<MenuProduct> menuProducts) {
        MenuProducts products = MenuProducts.of(menuProducts);
        validate(products);
        this.menuProducts = products;
        updateMenuId();
    }

    private void updateMenuId() {
        this.menuProducts.getProducts()
                .forEach(it -> it.updateMenuId(id));
    }

    private void validate(final MenuProducts menuProducts) {
        if (!menuProducts.isPossibleCreate(price.getValue())) {
            throw new MenuException(MenuExceptionType.EXCEED_PRICE);
        }
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

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getProducts();
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", menuGroup=" + menuGroup +
                ", menuProducts=" + menuProducts +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Menu menu = (Menu) o;
        return Objects.equals(id, menu.id) && Objects.equals(name, menu.name) && Objects.equals(price, menu.price) && Objects.equals(menuGroup, menu.menuGroup) && Objects.equals(menuProducts, menu.menuProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, menuGroup, menuProducts);
    }
}
