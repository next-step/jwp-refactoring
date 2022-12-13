package kitchenpos.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

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
            throw new IllegalArgumentException("메뉴그룹이 존재하지 않습니다.");
        }
    }

    private void validatePrice(final Price price, final MenuProducts menuProducts) {
        if (price.compareTo(menuProducts.totalPrice()) > 0) {
            throw new IllegalArgumentException("메뉴 가격이 각 상품 가격의 합보다 작습니다.");
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
