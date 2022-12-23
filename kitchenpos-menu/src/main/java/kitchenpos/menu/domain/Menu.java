package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import kitchenpos.domain.Name;
import kitchenpos.domain.Price;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Name name;
    @Embedded
    private Price price;
    @ManyToOne
    private MenuGroup menuGroup;
    private final MenuProducts menuProducts = new MenuProducts();

    protected Menu() {
    }

    protected Menu(Long id, String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = Name.from(name);
        this.price = Price.from(price);
        this.menuGroup = menuGroup;
        menuProducts.forEach(this::addMenuProduct);
    }

    public static Menu of(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return new Menu(null, name, price, menuGroup, menuProducts);
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


    private void addMenuProduct(MenuProduct menuProduct) {
        menuProducts.add(menuProduct);
        menuProduct.setMenu(this);
    }
}
