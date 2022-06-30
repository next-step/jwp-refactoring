package kitchenpos.menu.domain;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.menugroup.domain.MenuGroup;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.common.Messages.MENU_PRICE_EXPENSIVE;

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
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {
    }

    private Menu(Long id, Name name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        this(name, price, menuGroup, menuProducts);
        this.id = id;
    }

    public Menu(Name name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        validateExpensivePrice(price, menuProducts);
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        addMenuProducts(menuProducts);
    }

    public static Menu of(Long id, Name name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        return new Menu(id, name, price, menuGroup, menuProducts);
    }

    public static Menu of(Name name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        return new Menu(name, price, menuGroup, menuProducts);
    }

    private void validateExpensivePrice(Price price, MenuProducts menuProducts) {
        if (price.isExpensive(menuProducts.sumTotalPrice())) {
            throw new IllegalArgumentException(MENU_PRICE_EXPENSIVE);
        }
    }

    private void addMenuProducts(MenuProducts menuProducts) {
        menuProducts.getMenuProducts().forEach(menuProduct -> {
            menuProduct.toMenu(this);
            this.menuProducts.add(menuProduct);
        });
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public BigDecimal getPrice() {
        return price.getPrice();
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getMenuProducts();
    }
}
