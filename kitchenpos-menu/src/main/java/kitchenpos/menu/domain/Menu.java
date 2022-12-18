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

    @Column(nullable = false)
    private Long menuGroupId;

    @Embedded
    private MenuProducts menuProducts;

    protected Menu() {

    }

    private Menu(Long id, String name, BigDecimal price, Long menuGroupId, MenuProducts menuProducts) {
        this.id = id;
        this.name = name;
        this.price = Price.of(price);
        this.price.checkValidate(menuProducts.getTotalPrice());
        if (menuGroupId == null) {
            throw new IllegalArgumentException(MenuError.REQUIRED_MENU_GROUP);
        }
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static Menu of(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu(id, name, price, menuGroupId, MenuProducts.of(menuProducts));
    }

    public static Menu of(Long id, String name, BigDecimal price, Long menuGroupId, MenuProducts menuProducts) {
        return new Menu(id, name, price, menuGroupId, menuProducts);
    }

    public static Menu of(String name, BigDecimal price, MenuGroup menuGroup, MenuProducts menuProducts) {
        return new Menu(null, name, price, menuGroup.getId(), menuProducts);
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

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getMenuProducts();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return Objects.equals(id, menu.id) && Objects.equals(name, menu.name) && Objects.equals(price, menu.price) && Objects.equals(menuGroupId, menu.menuGroupId) && Objects.equals(menuProducts, menu.menuProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, menuGroupId, menuProducts);
    }
}
