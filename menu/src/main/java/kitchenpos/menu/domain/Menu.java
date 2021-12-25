package kitchenpos.menu.domain;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.menugroup.domain.MenuGroup;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Name name;
    @Embedded
    private Price price;
    @ManyToOne
    private MenuGroup menuGroup;
    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {
    }

    private Menu(Long id, Name name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    private Menu(Name name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public static Menu create(Long id, String name, long price, MenuGroup menuGroup, MenuProducts menuProducts) {
        Menu menu = new Menu(id, Name.of(name), Price.of(price), menuGroup, menuProducts);
        return create(price, menuProducts, menu);
    }

    public static Menu create(String name, long price, MenuGroup menuGroup, MenuProducts menuProducts) {
        Menu menu = new Menu(Name.of(name), Price.of(price), menuGroup, menuProducts);
        return create(price, menuProducts, menu);
    }

    private static Menu create(long price, MenuProducts menuProducts, Menu menu) {
        validateMenuPrice(price, menuProducts);
        menuProducts.updateMenu(menu);
        return menu;
    }

    private static void validateMenuPrice(long price, MenuProducts menuProducts) {
        long totalProductPrice = menuProducts.getTotalPrice();
        if (price > totalProductPrice) {
            throw new IllegalArgumentException("메뉴의 가격이 상품 가격의 총 합보다 클 수 없습니다");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.value();
    }

    public long getPrice() {
        return price.longValue();
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.values();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return id.equals(menu.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
