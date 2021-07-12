package kitchenpos.menu.domain;

import java.math.BigDecimal;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kitchenpos.menugroup.domain.MenuGroup;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private MenuName menuName;
    @Embedded
    private MenuPrice menuPrice;
    @Embedded
    private MenuProducts menuProducts;
    @ManyToOne
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    public Menu() {
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup) {
        this.menuName = new MenuName(name);
        this.menuPrice = new MenuPrice(price);
        this.menuProducts = new MenuProducts();
        this.menuGroup = menuGroup;
    }

    public Long getId() {
        return id;
    }

    public MenuName getName() {
        return menuName;
    }

    public MenuPrice getPrice() {
        return menuPrice;
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public void addMenuProduct(MenuProduct menuProduct) {
        this.menuProducts.add(menuProduct);
    }

    public boolean isMenuPriceGreaterThan(BigDecimal targetPrice) {
        return this.menuPrice.isGreaterThan(targetPrice);
    }
}
