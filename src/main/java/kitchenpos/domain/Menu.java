package kitchenpos.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

import static kitchenpos.common.ErrorMessage.INVALID_MENU_GROUP;
import static kitchenpos.common.ErrorMessage.MENU_PRICE_LESS_THAN_SUM_OF_PRICE;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Embedded
    private Price price;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;
    @Embedded
    private MenuProducts menuProducts;

    public Menu() {
    }

    private Menu(String name, BigDecimal price, MenuGroup menuGroup, MenuProducts menuProducts) {
        this.price = Price.from(price);
        this.menuProducts = menuProducts;
        menuProducts.setMenu(this);
        validateMenu(menuGroup, this.price, this.menuProducts);
        this.name = name;
        this.menuGroup = menuGroup;
    }

    public static Menu of(String name, BigDecimal price, MenuGroup menuGroup, MenuProducts menuProducts) {
        return new Menu(name, price, menuGroup, menuProducts);
    }

    private void validateMenu(final MenuGroup menuGroup, final Price price, final MenuProducts menuProducts) {
        validatePrice(price, menuProducts);
        validateMenuGroup(menuGroup);
    }

    private void validateMenuGroup(final MenuGroup menuGroup) {
        if (Objects.isNull(menuGroup)) {
            throw new IllegalArgumentException(INVALID_MENU_GROUP.getMessage());
        }
    }

    private void validatePrice(final Price price, final MenuProducts menuProducts) {
        if (price.compareTo(menuProducts.totalPrice()) > 0) {
            throw new IllegalArgumentException(MENU_PRICE_LESS_THAN_SUM_OF_PRICE.getMessage());
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price.value();
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public MenuProducts 메뉴세트목록() {
        return menuProducts;
    }
}
