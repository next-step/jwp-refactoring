package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import kitchenpos.common.domain.Price;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Embedded
    private Price price;

    @Embedded
    private MenuGroupId menuGroupId;

    @Embedded
    private MenuProducts menuProducts;

    protected Menu() {
    }

    private Menu(Long id, String name, Price price, MenuGroupId menuGroupId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = MenuProducts.of(new ArrayList<>());
    }

    public static Menu of(String name, Price price, MenuGroupId menuGroupId) {
        return new Menu(null, name, price, menuGroupId);
    }

    public static Menu of(String name, Price price, MenuGroupId menuGroupId, MenuProducts menuProducts) {
        Menu menu = new Menu(null, name, price, menuGroupId);
        menuProducts.acceptMenu(menu);

        return menu;
    }

    public static Menu of(String name, Price menuPrice, MenuGroupId menuGroupId, MenuProducts menuProducts, MenuValidator menuValidator) {
        menuValidator.checkAllFindProducts(menuProducts);
        menuValidator.checkMenuPrice(menuPrice, menuProducts);

        Menu menu = new Menu(null, name, menuPrice, menuGroupId);
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

    public MenuGroupId getMenuGroupId() {
        return this.menuGroupId;
    }

    public MenuProducts getMenuProducts() {
        return this.menuProducts;
    }

    public boolean isEqualMenuId(Long menuId) {
        return this.id.equals(menuId);
    }

    public List<ProductId> getProductIds() {
        return this.menuProducts.getProductIds();
    }
}
