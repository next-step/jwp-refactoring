package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import kitchenpos.common.exception.KitchenposErrorCode;
import kitchenpos.common.exception.KitchenposException;
import kitchenpos.common.price.domain.Price;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Price price;

    @Column(nullable = false)
    private Long menuGroupId;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    public Menu() {
    }

    public Menu(Long id, String name, Price price, Long menuGroupId, MenuProducts menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        addMenuProducts(menuProducts);
    }

    public Menu(String name, BigDecimal price, Long menuGroupId, MenuProducts menuProducts) {
        this(null, name, new Price(price), menuGroupId, menuProducts);
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
        return menuGroupId;
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
    }

    public void validatePrice(Price sum) {
        if (price.isBiggerThan(sum)) {
            throw new KitchenposException(KitchenposErrorCode.INVALID_MENU_PRICE);
        }
    }
}
