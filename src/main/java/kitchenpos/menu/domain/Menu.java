package kitchenpos.menu.domain;

import java.math.BigDecimal;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kitchenpos.common.Name;
import kitchenpos.common.Price;
import kitchenpos.exception.ErrorMessage;
import kitchenpos.menugroup.domain.MenuGroup;

@Entity
public class Menu {
    public static String ENTITY_NAME = "메뉴";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Name name;
    private Price price;
    @ManyToOne
    @JoinColumn(name = "menu_group_id", foreignKey = @ForeignKey(name = "fk_menu_to_menu_group"))
    private MenuGroup menuGroup;
    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {}

    private Menu(String name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        menuProducts.updateMenu(this);
        this.name = Name.of(name);
        this.price = price;
        this.menuGroup = menuGroup;
        validatePrice(menuProducts);
        this.menuProducts = menuProducts;
    }

    private void validatePrice(MenuProducts menuProducts) {
        Price menuProductsPrice = menuProducts.value()
            .stream()
            .map(MenuProduct::getTotalPrice)
            .reduce(Price::add)
            .orElse(Price.ZERO);
        if (price.compareTo(menuProductsPrice) > 0) {
            throw new IllegalArgumentException(ErrorMessage.PRICE_HIGHER_THAN_MENU_PRODUCTS_TOTAL_PRICES);
        }
    }

    public static Menu of(String name, BigDecimal price, MenuGroup menuGroup, MenuProducts menuProducts) {
        return new Menu(name, Price.of(price), menuGroup, menuProducts);
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }

}
