package kitchenpos.domain.menu;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import kitchenpos.domain.Name;
import kitchenpos.domain.Price;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.utils.StreamUtils;

@Entity
@Table(name = "menu")
public class Menu {
    private static final String NOT_EXIST_MENU_GROUP_PRICE = "Menu 는 MenuGroup 가 필수값 입니다.";
    private static final String NOT_EXIST_MENU_PRICE = "Menu 는 Price 가 필수값 입니다.";
    private static final String INVALID_MENU_PRICE = "Menu Price 는 상품 가격 총합보다 작아야합니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Embedded
    private Name name;

    @Embedded
    private Price price;

    @ManyToOne
    @JoinColumn(name = "menu_group_id", foreignKey = @ForeignKey(name = "fk_menu_menu_group"), nullable = false)
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts = MenuProducts.createEmpty();

    protected Menu() {}

    public Menu(Long id) {
        this.id = id;
    }

    private Menu(Long id, String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this(name, price, menuGroup, menuProducts);
        this.id = id;
    }

    private Menu(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this.name = Name.from(name);
        this.price = Price.from(price);
        this.menuGroup = menuGroup;
        this.menuProducts = MenuProducts.from(menuProducts);
    }

    public static Menu from(long id) {
        return new Menu(id);
    }

    public static Menu of(String name, BigDecimal price, MenuGroup menuGroup) {
        validateCreateMenu(price, menuGroup);
        return new Menu(name, price, menuGroup, new ArrayList<>());
    }

    public static Menu of(Long id, String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        validateCreateMenu(price, menuGroup);
        return new Menu(id, name, price, menuGroup, menuProducts);
    }

    public void addMenuProducts(final List<MenuProduct> menuProducts) {
        validateMenuPrice(menuProducts);

        this.menuProducts.addAll(menuProducts);
        menuProducts.forEach(menuProduct -> menuProduct.assignMenu(this));
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

    private static void validateCreateMenu(BigDecimal price, MenuGroup menuGroup) {
        if (Objects.isNull(price)) {
            throw new IllegalArgumentException(NOT_EXIST_MENU_PRICE);
        }

        if (Objects.isNull(menuGroup)) {
            throw new IllegalArgumentException(NOT_EXIST_MENU_GROUP_PRICE);
        }
    }

    private void validateMenuPrice(List<MenuProduct> menuProducts) {
        BigDecimal sum = StreamUtils.sumToBigDecimal(menuProducts, MenuProduct::calculateTotalPrice);

        if (price.isGreaterThan(sum)) {
            throw new IllegalArgumentException(INVALID_MENU_PRICE);
        }
    }
}
