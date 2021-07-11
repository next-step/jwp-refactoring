package kitchenpos.menu.domain;

import kitchenpos.common.valueobject.Name;
import kitchenpos.common.valueobject.Price;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    @Embedded
    private Price price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts;

    protected Menu() {
    }

    private Menu(Long id, Name name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    private Menu(Name name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        this(null, name, price, menuGroup, menuProducts);
        menuProducts.registerAll(this);
    }

    public static Menu of(String name, BigDecimal price, MenuGroup menuGroup) {
        return new Menu(null, Name.of(name), Price.of(price), menuGroup, MenuProducts.of(new ArrayList<>()));
    }

    public static Menu of(String name, BigDecimal price) {
        return new Menu(null, Name.of(name), Price.of(price), null, MenuProducts.of(new ArrayList<>()));
    }

    public static Menu create(String name, BigDecimal menuPrice, MenuGroup menuGroup, List<MenuProduct> menuProductList) {
        MenuProducts menuProducts = MenuProducts.of(menuProductList);
        Price price = Price.of(menuPrice);
        menuProducts.validatePrice(price);
        return new Menu(Name.of(name), price, menuGroup, menuProducts);
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

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getUnmodifiableList();
    }
}
