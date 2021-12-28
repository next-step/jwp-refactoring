package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import kitchenpos.common.price.domain.Price;
import kitchenpos.exception.KitchenposErrorCode;
import kitchenpos.exception.KitchenposException;
import kitchenpos.menugroup.domain.MenuGroup;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Price price;

    @ManyToOne(fetch = FetchType.LAZY)
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    public Menu() {
    }

    public Menu(Long id, String name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        addMenuProducts(menuProducts);
        validateMenu();
    }

    public Menu(Long id, String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this(id, name, new Price(price), menuGroup, new MenuProducts(menuProducts));
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup, MenuProducts menuProducts) {
        this(null, name, new Price(price), menuGroup, menuProducts);
    }

    private void validateMenu() {
        Price sum = menuProducts.calculateSum();
        if (price.isBiggerThan(sum)) {
            throw new KitchenposException(KitchenposErrorCode.INVALID_MENU_PRICE);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<MenuProduct> getMenuProductList() {
        return menuProducts.getMenuProducts();
    }

    public Long getMenuGroupId() {
        return menuGroup.getId();
    }

    public BigDecimal getPriceValue() {
        return price.getPrice();
    }

    public void addMenuProducts(MenuProducts menuProducts) {
        for (MenuProduct menuProduct : menuProducts.getMenuProducts()) {
            addMenuProduct(menuProduct);
        }
    }

    private void addMenuProduct(MenuProduct menuProduct) {
        this.menuProducts.add(menuProduct);
        menuProduct.referenceMenu(this);
    }
}
