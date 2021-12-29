package kitchenpos.menu.domain;

import kitchenpos.domain.Name;
import kitchenpos.domain.Price;
import kitchenpos.exception.InvalidPriceException;

import javax.persistence.*;
import java.util.List;

@Entity
public class Menu {
    private static final String MENU_PRICE_EXCEPTION = "메뉴 가격은 상품 가격의 합보다 적어야 합니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    @Embedded
    private Price price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id", nullable = false)
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {

    }

    private Menu(Long id, String name, Long price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = Name.of(name);
        this.price = Price.of(price);
        this.menuGroup = menuGroup;
        this.menuProducts = MenuProducts.of(menuProducts);
        this.menuProducts.addMenu(this);
        validatePrice(price, menuProducts);
    }

    private void validatePrice(Long price, List<MenuProduct> menuProducts) {
        Price sum = menuProducts.stream()
                .map(MenuProduct::calculate)
                .reduce(Price::sum)
                .orElseGet(Price::zero);

        if (price.compareTo(sum.getPrice()) > 0) {
            throw new InvalidPriceException(MENU_PRICE_EXCEPTION);
        }
    }

    public static Menu of(Long id, String name, Long price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return new Menu(id, name, price, menuGroup, menuProducts);
    }

    public static Menu of(String name, Long price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return new Menu(null, name, price, menuGroup, menuProducts);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public Long getPrice() {
        return price.getPrice();
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getMenuProducts();
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }
}
