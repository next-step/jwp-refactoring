package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.common.exception.InvalidParameterException;
import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;

@Entity
public class Menu {
    private static final String ERROR_MESSAGE_MENU_GROUP_IS_NULL = "메뉴 그룹은 필수입니다.";
    private static final String ERROR_MESSAGE_MENU_IS_GREATER_THAN_TOTAL_PRICE = "상품 총 금액이 메뉴의 가격 보다 클 수 없습니다.";

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

    private Menu(Long id, String name, BigDecimal originPrice, MenuGroup menuGroup, List<MenuProduct> products) {
        Price price = Price.from(originPrice);
        MenuProducts menuProducts = MenuProducts.from(products);
        validate(menuGroup, price, menuProducts);
        this.id = id;
        this.name = Name.from(name);
        this.price = price;
        this.menuGroup = menuGroup;
        menuProducts.changeMenu(this);
        this.menuProducts = menuProducts;
    }

    private Menu(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> products) {
        this(null, name, price, menuGroup, products);
    }

    public static Menu of(Long id, String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> products) {
        return new Menu(id, name, price, menuGroup, products);
    }

    public static Menu of(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> products) {
        return new Menu(name, price, menuGroup, products);
    }

    public static Menu of(String name, BigDecimal price, MenuGroup menuGroup, MenuProduct... products) {
        return new Menu(name, price, menuGroup, Arrays.asList(products));
    }

    private void validate(MenuGroup menuGroup, Price price, MenuProducts menuProducts) {
        validateMenuGroup(menuGroup);
        validateMenuPrice(price, menuProducts);
    }

    private void validateMenuGroup(MenuGroup menuGroup) {
        if (menuGroup == null) {
            throw new InvalidParameterException(ERROR_MESSAGE_MENU_GROUP_IS_NULL);
        }
    }

    private void validateMenuPrice(Price price, MenuProducts menuProducts) {
        if (price.isGreaterThan(menuProducts.totalPrice())) {
            throw new InvalidParameterException(ERROR_MESSAGE_MENU_IS_GREATER_THAN_TOTAL_PRICE);
        }
    }

    public Long id() {
        return id;
    }

    public Name name() {
        return name;
    }

    public Price price() {
        return price;
    }

    public MenuGroup menuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> menuProducts() {
        return menuProducts.list();
    }
}
