package kitchenpos.menu.domain;

import kitchenpos.exception.MenuError;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
    @Embedded
    private Price price;

    @ManyToOne(fetch = FetchType.LAZY)
    private MenuGroup menuGroup;

    @Embedded
    private final MenuProducts menuProducts = new MenuProducts();

    protected Menu() {

    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup) {
        if (menuGroup == null) {
            throw new IllegalArgumentException(MenuError.REQUIRED_MENU_GROUP);
        }

        this.name = name;
        this.price = Price.of(price);
        this.menuGroup = menuGroup;
    }

    public void validatePrice() {
        if (price.compareTo(menuProducts.getTotalPrice()) > 0) {
            throw new IllegalArgumentException(MenuError.INVALID_PRICE);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getPrice();
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return Objects.equals(name, menu.name) && Objects.equals(price, menu.price) && Objects.equals(menuGroup, menu.menuGroup) && Objects.equals(menuProducts, menu.menuProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, menuGroup, menuProducts);
    }

    public void create(List<MenuProduct> menuProducts) {
        menuProducts.forEach(this::addMenuProduct);
    }

    public void addMenuProduct(MenuProduct menuProduct) {
        this.menuProducts.addMenuProduct(this, menuProduct);
    }
}
