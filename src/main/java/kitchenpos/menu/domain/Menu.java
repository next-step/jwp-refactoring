package kitchenpos.menu.domain;

import java.math.BigDecimal;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import kitchenpos.common.domain.Name;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Name menuName;
    @Embedded
    private MenuPrice menuPrice;
    @Embedded
    private MenuProducts menuProducts;
    private Long menuGroupId;

    public Menu() {
    }

    public Menu(String name, BigDecimal price, Long menuGroupId) {
        this.menuName = new Name(name);
        this.menuPrice = new MenuPrice(price);
        this.menuProducts = new MenuProducts();
        this.menuGroupId = menuGroupId;
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return menuName;
    }

    public MenuPrice getPrice() {
        return menuPrice;
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public void addMenuProduct(MenuProduct menuProduct) {
        this.menuProducts.add(menuProduct);
    }

    public boolean isMenuPriceGreaterThan(BigDecimal targetPrice) {
        return this.menuPrice.isGreaterThan(targetPrice);
    }
}
