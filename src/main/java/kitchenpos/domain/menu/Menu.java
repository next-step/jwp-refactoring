package kitchenpos.domain.menu;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Objects;

import static javax.persistence.FetchType.LAZY;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Embedded
    private Price price;

    @OneToOne(fetch = LAZY, orphanRemoval = true)
    @JoinColumn(name = "menu_group_id", foreignKey = @ForeignKey(name = "fk_menu_menu_group"))
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts = MenuProducts.of(new ArrayList<>());

    // for jpa
    public Menu() {
    }

    private Menu(Long id, String name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;

    }

    public static Menu of(String name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        return new Menu(null, name, price, menuGroup, menuProducts);
    }

    public static Menu of(String name, Price price, MenuGroup menuGroup) {
        return new Menu(null, name, price, menuGroup, MenuProducts.of(new ArrayList<>()));
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

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }

    public boolean isReasonablePrice() {
        Price sum = menuProducts.sumOfMenuProductPrice();
        return !price.isGreaterThen(sum);
    }

    public void addMenuProduct(MenuProduct menuProduct) {
        menuProducts.addMenuProduct(menuProduct);
        menuProduct.setMenu(this);
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
