package kitchenpos.menu.domain;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuPrice;
import kitchenpos.product.domain.Product;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private MenuPrice price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "menu")
    private List<MenuProduct> menuProducts;

    protected Menu() {
    }

    private Menu(Long id, String name, MenuPrice price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;

        comparePriceAndSumOfMenuProducts();
    }

    private void comparePriceAndSumOfMenuProducts() {
        price.compareSumOfMenuProducts(menuProducts);
    }

    public static Menu of(Long id, String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return new Menu(id, name, new MenuPrice(price), menuGroup, menuProducts);
    }

    public void addMenuToMenuProducts() {
        menuProducts.forEach(menuProduct -> menuProduct.addMenu(this));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public MenuPrice getPrice() {
        return price;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public List<Product> getProducts() {
        return menuProducts.stream()
                .map(MenuProduct::getProduct)
                .collect(Collectors.toList());
    }
}
