package kitchenpos.menu.domain;

import java.util.Objects;
import java.util.Set;
import javax.persistence.*;
import kitchenpos.product.domain.Price;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Price price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @Embedded
    private final MenuProducts menuProducts = new MenuProducts();

    protected Menu() {
    }

    private Menu(String name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        validatePrice(price, menuProducts);
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.addMenuProducts(menuProducts);
    }

    public static Menu createMenu(String name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        return new Menu(name, price, menuGroup, menuProducts);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroup.getId();
    }

    public Set<MenuProduct> getMenuProducts() {
        return menuProducts.getValue();
    }

    public void addMenuProducts(MenuProducts menuProducts) {
        this.menuProducts.addAll(this, menuProducts);
    }

    private void addMenuGroup(MenuGroup menuGroup) {
        this.menuGroup = menuGroup;
    }

    private void updatePrice(Price price) {
        this.price = price;
    }

    private void updateName(String name) {
        this.name = name;
    }

    private void validatePrice(Price price, MenuProducts menuProducts) {
        Price sum = menuProducts.calculateTotalPrice();
        if (price.isGreaterThan(sum)) {
            throw new IllegalArgumentException("메뉴의 가격은 상품 가격의 총합보다 클 수 없습니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Menu menu = (Menu) o;
        return Objects.equals(id, menu.id) &&
                Objects.equals(name, menu.name) &&
                Objects.equals(price, menu.price) &&
                Objects.equals(menuGroup, menu.menuGroup) &&
                Objects.equals(menuProducts, menu.menuProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, menuGroup, menuProducts);
    }
}
