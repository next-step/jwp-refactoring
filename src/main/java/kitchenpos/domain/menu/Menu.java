package kitchenpos.domain.menu;

import java.util.ArrayList;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kitchenpos.domain.Price;
import kitchenpos.exception.menu.NotCorrectMenuPriceException;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Embedded
    private Price price;

    @ManyToOne
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts;

    protected Menu() {
    }

    private Menu(Long id, String name, Price price, MenuGroup menuGroup) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = MenuProducts.of(new ArrayList<>());
    }

    public static Menu of(String name, Price price, MenuGroup menuGroup) {
        return new Menu(null, name, price, menuGroup);
    }

    public static Menu of(String name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        Price sumOfProductsPrice = menuProducts.getSumProductPrice();

        if (price.compareTo(sumOfProductsPrice) > 0) {
            throw new NotCorrectMenuPriceException();
        }

        Menu menu = new Menu(null, name, price, menuGroup);
        menuProducts.acceptMenu(menu);

        return menu;
    }

    public static Menu of(String name, Price price) {
        return new Menu(null, name, price, null);
    }

    public static Menu of(Long id, String name, Price price) {
        return new Menu(id, name, price, null);
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Price getPrice() {
        return this.price;
    }

    public MenuGroup getMenuGroup() {
        return this.menuGroup;
    }

    public MenuProducts getMenuProducts() {
        return this.menuProducts;
    }

    public boolean isEqualMenuId(Long menuId) {
        return this.id.equals(menuId);
    }
}
