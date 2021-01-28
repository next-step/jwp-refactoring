package kitchenpos.menu.domain;

import kitchenpos.common.domain.BaseEntity;
import kitchenpos.menugroup.domain.MenuGroup;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class Menu extends BaseEntity {
    private static final int MIN_SIZE_PRODUCTS_IN_MENU = 2;

    @Column
    private String name;
    @Embedded
    private MenuPrice price;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id", nullable = false)
    private MenuGroup menuGroup;

    public static Menu create(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        validate(price, menuGroup, menuProducts);
        return new Menu(name, price, menuGroup);
    }

    private Menu(String name, BigDecimal price, MenuGroup menuGroup) {
        this.name = name;
        this.price = new MenuPrice(price);
        this.menuGroup = menuGroup;
    }

    protected Menu() {
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getPrice();
    }

    public Long getMenuGroupId() {
        return menuGroup.getId();
    }

    private static void validate(BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        validateSumPrice(price, menuProducts);
        if (menuGroup == null) {
            throw new IllegalArgumentException("메뉴를 등록하기 위해서는 메뉴그룹이 있어야합니다.");
        }
        if (menuProducts.size() < MIN_SIZE_PRODUCTS_IN_MENU) {
            throw new IllegalArgumentException("메뉴를 등록하기 위해서는 등록할 상품이 " + MIN_SIZE_PRODUCTS_IN_MENU + "개 이상 있아야합니다.");
        }
    }

    private static void validateSumPrice(BigDecimal price, List<MenuProduct> menuProducts) {
        if (price == null) {
            throw new IllegalArgumentException("메뉴의 가격이 존재하지 않습니다.");
        }
        if (getSumPrice(menuProducts).compareTo(price) < 0) {
            throw new IllegalArgumentException("메뉴의 가격은 상품들 가격의 총합보다 클 수 없습니다.");
        }
    }

    private static BigDecimal getSumPrice(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(MenuProduct::getSumPriceOfProducts)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
