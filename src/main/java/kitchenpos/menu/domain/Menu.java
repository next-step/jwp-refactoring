package kitchenpos.menu.domain;

import kitchenpos.common.domain.BaseEntity;
import kitchenpos.common.domain.Price;
import kitchenpos.common.exception.IllegalArgumentException;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Menu extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Price price;

    @ManyToOne
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts(new ArrayList<>());

    protected Menu() {
    }

    public Menu(Long id, String name, Price price, MenuGroup menuGroup) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
    }

    public static Menu of(Long id, String name, Price price, MenuGroup menuGroup) {
        return new Menu(id, name, price, menuGroup);
    }

    public static Menu of(String name, Price price, MenuGroup menuGroup) {
        return of(null, name, price, menuGroup);
    }

    private static void checkValidation(Price price, MenuProducts menuProducts) {
        if (price.isGreaterThan(menuProducts.sum())) {
            throw new IllegalArgumentException("메뉴의 가격이 상품 가격의 합계보다 클 수 없습니다.");
        }
    }

    public void addMenuProducts(List<MenuProduct> menuProducts) {
        for (MenuProduct menuProduct : menuProducts) {
            addMenuProduct(menuProduct);
        }
        checkValidation(price, this.menuProducts);
    }

    public void addMenuProduct(MenuProduct menuProduct) {
        menuProducts.addMenuProduct(menuProduct);
        if (!menuProduct.equalsMenu(this)) {
            menuProduct.addMenu(this);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.get();
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return Objects.equals(id, menu.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
