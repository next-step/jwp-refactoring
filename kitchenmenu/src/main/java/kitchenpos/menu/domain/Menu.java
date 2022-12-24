package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.menuconstants.MenuErrorMessages;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Embedded
    private MenuPrice menuPrice;
    private Long menuGroupId;
    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    public Menu() {}

    public Menu(String name, MenuPrice menuPrice, Long menuGroupId) {
        this.name = name;
        this.menuPrice = menuPrice;
        this.menuGroupId = menuGroupId;
    }

    public Menu(Long id, String name, MenuPrice menuPrice, Long menuGroupId, MenuProducts menuProducts) {
        this.id = id;
        this.name = name;
        this.menuPrice = menuPrice;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public void addMenuProduct(MenuProduct menuProduct) {
        this.menuProducts.add(menuProduct);
        menuProduct.addMenu(this);
    }

    public void validateName() {
        if (name.isEmpty()) {
            throw new IllegalArgumentException(MenuErrorMessages.MENU_NAME_CANNOT_BE_EMPTY);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public MenuPrice getPrice() {
        return menuPrice;
    }

    public BigDecimal getPriceValue() {
        return menuPrice.getPrice();
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Menu menu = (Menu) o;
        return Objects.equals(id, menu.id) && Objects.equals(name, menu.name)
                && (menuPrice.compareTo(menu.menuPrice) == 0) && Objects.equals(menuGroupId, menu.menuGroupId)
                && Objects.equals(menuProducts, menu.menuProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, menuPrice, menuGroupId, menuProducts);
    }
}
