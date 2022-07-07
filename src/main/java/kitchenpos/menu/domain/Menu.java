package kitchenpos.menu.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Embedded
    private MenuPrice price;
    @JoinColumn(name = "menu_group_id", nullable = false)
    private Long menuGroupId;
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    private Menu(Long id, String name, int price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = MenuPrice.from(price);
        this.menuGroupId = menuGroupId;
        registerMenuProducts(menuProducts);
    }

    private Menu(String name, int price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this.name = name;
        this.price = MenuPrice.from(price);
        this.menuGroupId = menuGroupId;
        registerMenuProducts(menuProducts);
    }

    public static Menu of(Long id, String name, int price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu(id, name, price, menuGroupId, menuProducts);
    }

    public static Menu of(String name, int price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu(name, price, menuGroupId, menuProducts);
    }

    private void registerMenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
        menuProducts.forEach(menuProduct -> menuProduct.registerMenu(this));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price.getValue();
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
