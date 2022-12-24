package kitchenpos.menu.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Menu {
    public static final String MENU_GROUP_NOT_NULL_EXCEPTION_MESSAGE = "메뉴 그룹이 없을 수 없습니다.";
    public static final String PRICE_NOT_NULL_EXCEPTION_MESSAGE = "가격은 필수입니다.";

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
        validate(name, price, menuGroup, menuProducts);
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
        menuProducts.mapMenu(this);
    }

    private void validate(Name name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        validateNullName(name);
        validateNullMenuGroup(menuGroup);
        validateNullPrice(price);
        validateEmptyMenuProducts(menuProducts);
    }

    private void validateNullName(Name name) {
        if (Objects.isNull(name)) {
            throw new IllegalArgumentException();
        }
    }

    private void validateNullMenuGroup(MenuGroup menuGroup) {
        if (Objects.isNull(menuGroup)) {
            throw new IllegalArgumentException(MENU_GROUP_NOT_NULL_EXCEPTION_MESSAGE);
        }
    }

    private void validateNullPrice(Price price) {
        if (Objects.isNull(price)) {
            throw new IllegalArgumentException(PRICE_NOT_NULL_EXCEPTION_MESSAGE);
        }
    }

    private void validateEmptyMenuProducts(MenuProducts menuProducts) {
        if (menuProducts.isEmpty()) {
            throw new IllegalArgumentException("메뉴 상품이 없습니다.");
        }
    }

    public void validate(MenuProductValidator menuProductValidator) {
        menuProductValidator.validate(this);
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return this.name;
    }

    public Price getPrice() {
        return this.price;
    }

    public MenuProducts getMenuProducts() {
        return this.menuProducts;
    }
}
