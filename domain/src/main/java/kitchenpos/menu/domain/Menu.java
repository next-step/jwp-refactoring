package kitchenpos.menu.domain;

import kitchenpos.common.domain.Price;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
public class Menu {
    private static final String INVALID_MENU_PRICE_MESSAGE = "메뉴의 가격은 메뉴 상품의 합보다 클 수 없습니다";

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

    public Menu(String name, BigDecimal price, MenuGroup menuGroup) {
        this(null, name, price, menuGroup);
    }

    public Menu(Long id, String name, BigDecimal price, MenuGroup menuGroup) {
        this.id = id;
        this.name = name;
        this.price = new Price(price);
        this.menuGroup = menuGroup;
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public BigDecimal price() {
        return price.price();
    }

    public List<MenuProduct> menuProducts() {
        return menuProducts.menuProducts();
    }

    public void mappingProducts(MenuProducts menuProducts) {
        this.menuProducts = menuProducts;
    }

    public MenuGroup menuGroup() {
        return menuGroup;
    }

    public void validateMenuProductsPrice() {
        BigDecimal menuProductsPriceSum = menuProducts.menuProductsPrice();
        if (price.comparePrice(menuProductsPriceSum) > 0) {
            throw new IllegalArgumentException(INVALID_MENU_PRICE_MESSAGE);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return Objects.equals(id, menu.id) && Objects.equals(name, menu.name) && Objects.equals(price, menu.price) && Objects.equals(menuGroup, menu.menuGroup) && Objects.equals(menuProducts, menu.menuProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, menuGroup, menuProducts);
    }
}
