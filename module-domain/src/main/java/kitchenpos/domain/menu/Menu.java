package kitchenpos.domain.menu;

import java.util.Objects;
import java.util.Set;
import javax.persistence.*;
import kitchenpos.domain.product.Price;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Price price;

    @Column(nullable = false)
    private Long menuGroupId;

    @Embedded
    private final MenuProducts menuProducts = new MenuProducts();

    protected Menu() {
    }

    private Menu(String name, long price, Long menuGroupId, MenuProducts menuProducts) {
        this.name = name;
        this.price = Price.from(price);
        this.menuGroupId = menuGroupId;
        this.addMenuProducts(menuProducts);
    }

    public static Menu createMenu(String name, long price, Long menuGroupId, MenuProducts menuProducts) {
        return new Menu(name, price, menuGroupId, menuProducts);
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
        return menuGroupId;
    }

    public Set<MenuProduct> getMenuProducts() {
        return menuProducts.getValue();
    }

    public void addMenuProducts(MenuProducts menuProducts) {
        this.menuProducts.addAll(this, menuProducts);
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
                Objects.equals(menuGroupId, menu.menuGroupId) &&
                Objects.equals(menuProducts, menu.menuProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, menuGroupId, menuProducts);
    }
}
