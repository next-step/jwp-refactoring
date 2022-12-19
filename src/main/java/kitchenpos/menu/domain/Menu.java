package kitchenpos.menu.domain;

import kitchenpos.common.Name;
import kitchenpos.common.Price;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
public class Menu {
    public static final String MENU_GROUP_NOT_NULL_EXCEPTION_MESSAGE = "메뉴 그룹이 없을 수 없습니다.";
    public static final String PRICE_NOT_NULL_EXCEPTION_MESSAGE = "가격은 필수입니다.";
    public static final String MENU_PRICE_EXCEPTION_MESSAGE = "메뉴의 가격이 메뉴 상품의 합보다 클 수 없다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Name name;
    @Embedded
    private Price price;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "menu_group_id", foreignKey = @ForeignKey(name = "fk_menu_menu_group"))
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    public Menu(Name name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        validate(price, menuGroup, menuProducts);
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
        menuProducts.mapMenu(this);
    }

    private static void validate(Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        validateNullMenuGroup(menuGroup);
        validateNullPrice(price);
        validateEmptyMenuProducts(menuProducts);
        validatePrice(price, menuProducts);
    }

    private static void validatePrice(Price price, MenuProducts menuProducts) {
        if (price.getPrice().compareTo(menuProducts.sum()) > 0) {
            throw new IllegalArgumentException(MENU_PRICE_EXCEPTION_MESSAGE);
        }
    }

    private static void validateEmptyMenuProducts(MenuProducts menuProducts) {
        if (menuProducts.isEmpty()) {
            throw new IllegalArgumentException("메뉴 상품이 없습니다.");
        }
    }

    private static void validateNullPrice(Price price) {
        if (Objects.isNull(price)) {
            throw new IllegalArgumentException(PRICE_NOT_NULL_EXCEPTION_MESSAGE);
        }
    }

    private static void validateNullMenuGroup(MenuGroup menuGroup) {
        if (Objects.isNull(menuGroup)) {
            throw new IllegalArgumentException(MENU_GROUP_NOT_NULL_EXCEPTION_MESSAGE);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return this.name.getName();
    }

    public BigDecimal getPrice() {
        return this.price.getPrice();
    }

    public List<MenuProduct> getMenuProducts() {
        return this.menuProducts.getMenuProducts();
    }
}
