package kitchenpos.menu.domain;

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

    public Menu() {
    }

    public Menu(Long id, String name, long price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this(name, price, menuGroup, menuProducts);
        this.id = id;
    }

    public Menu(String name, long price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this(name, price, menuGroup);
        this.menuProducts = new MenuProducts(menuProducts);
    }

    public Menu(String name, long price, MenuGroup menuGroup) {
        this.name = Name.of(name);
        this.price = Price.of(price);
        this.menuGroup = menuGroup;
    }

    public Menu(Name name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public void setMenuProducts(MenuProducts menuProducts) {
        long totalProductPrice = menuProducts.getTotalPrice();
        if (price.longValue() > totalProductPrice) {
            throw new IllegalArgumentException("메뉴의 가격이 상품 가격의 총 합보다 클 수 없습니다");
        }
        this.menuProducts = menuProducts;
    }

    public long getTotalProductPrice() {
        return menuProducts.getTotalPrice();
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
