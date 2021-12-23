package kitchenpos.menu.domain;

import kitchenpos.menugroup.domain.MenuGroup;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
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

    @OneToOne(fetch = FetchType.LAZY)
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts = MenuProducts.empty();

    protected Menu() {
    }

    private Menu(Name name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        if (price.isMoreExpensive(menuProducts.getTotalPrice())) {
            throw new IllegalArgumentException();
        }
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        addMenuProducts(menuProducts.getMenuProducts());
    }

    private Menu(Long id, Name name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        this(name, price, menuGroup, menuProducts);
        this.id = id;
    }

    public static Menu of(Name name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        return new Menu(name, price, menuGroup, menuProducts);
    }

    public static Menu of(Long id, Name name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        return new Menu(id, name, price, menuGroup, menuProducts);
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

    private void addMenuProducts(List<MenuProduct> menuProducts) {
        menuProducts.forEach(menuProduct ->
        {
            menuProduct.setMenu(this);
            this.menuProducts.add(menuProduct);
        });
    }
}
