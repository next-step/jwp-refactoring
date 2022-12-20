package kitchenpos.menu.domain;

import kitchenpos.global.domain.Price;

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

    private Long menuGroupId;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {

    }

    public Menu(String name, Price price, Long menuGroupId, MenuProducts menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        addMenuProducts(menuProducts);
    }

    public static Menu of(String name, Long price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu(name, Price.of(price), menuGroupId, new MenuProducts(menuProducts));
    }

    private void addMenuProducts(MenuProducts menuProducts) {
        this.menuProducts = menuProducts;
        menuProducts.changeMenu(this);
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
        return this.menuGroupId;
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
                ", menuGroupId=" + menuGroupId +
                ", menuProducts=" + menuProducts +
                '}';
    }
}
