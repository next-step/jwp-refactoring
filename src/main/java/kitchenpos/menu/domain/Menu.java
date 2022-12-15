package kitchenpos.menu.domain;

import java.math.BigDecimal;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.common.Name;
import kitchenpos.common.Price;
import kitchenpos.exception.ErrorMessage;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Name name;
    private Price price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id", foreignKey = @ForeignKey(name = "fk_menu_to_menu_group"))
    private MenuGroup menuGroup;
    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    public Menu() {
    }

    private Menu(String name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        this.name = Name.of(name);
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public static Menu of(String name, BigDecimal priceInput, MenuGroup menuGroup, MenuProducts menuProducts){
        Price price = Price.of(priceInput);
        Menu menu = new Menu(name, price, menuGroup, menuProducts);
        validatePrice(price, menuProducts);
        menu.menuProducts.updateMenu(menu);
        return menu;
    }

    private static void validatePrice(Price price, MenuProducts menuProducts) {
        if(price.compareTo(menuProducts.totalPrice()) > 0) {
            throw new IllegalArgumentException(ErrorMessage.PRICE_HIGHER_THAN_MENU_PRODUCTS_TOTAL_PRICES);
        }
    }

    private void addMenuProducts(MenuProducts menuProducts) {
        menuProducts.updateMenuProductsMenu(this);
        this.menuProducts = menuProducts;
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
