package kitchenpos.menu.domain;

import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Price;
import kitchenpos.menu.message.MenuMessage;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

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
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {

    }

    public Menu(String name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        validate(price, menuGroup, menuProducts);

        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        addMenuProducts(menuProducts);
    }

    public static Menu of(String name, Long price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return new Menu(name, Price.of(price), menuGroup, new MenuProducts(menuProducts));
    }

    private void addMenuProducts(MenuProducts menuProducts) {
        this.menuProducts = menuProducts;
        menuProducts.changeMenu(this);
    }

    private void validate(Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        validateMenuGroup(menuGroup);
        validateMenuProducts(price, menuProducts);
    }

    private void validateMenuGroup(MenuGroup menuGroup) {
        if(menuGroup == null) {
            throw new IllegalArgumentException(MenuMessage.CREATE_MENU_ERROR_MENU_GROUP_MUST_BE_NON_NULL.message());
        }
    }

    private void validateMenuProducts(Price price, MenuProducts menuProducts) {
        if(price.isGreaterThan(menuProducts.totalPrice())) {
            throw new IllegalArgumentException(MenuMessage.ADD_PRODUCT_ERROR_IN_VALID_PRICE.message());
        }
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Price getPrice() {
        return this.price;
    }

    public Long getMenuGroupId() {
        return this.menuGroup.getId();
    }

    public MenuProducts getMenuProducts() {
        return this.menuProducts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Menu menu = (Menu) o;

        if (!Objects.equals(id, menu.id)) return false;
        return Objects.equals(name, menu.name);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", menuGroup=" + menuGroup +
                ", menuProducts=" + menuProducts +
                '}';
    }
}
