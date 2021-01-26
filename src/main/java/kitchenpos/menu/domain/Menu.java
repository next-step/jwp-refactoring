package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
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

    @Column(name = "menu_group_id", nullable = false)
    private Long menuGroupId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "menu_id")
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    private Menu(Long id, String name, MenuPrice price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static Menu of(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu(id, name, new MenuPrice(price), menuGroupId, menuProducts);
    }

    public void comparePriceAndSumOfMenuProducts(List<Product> products) {
        BigDecimal sum = BigDecimal.ZERO;

        for (MenuProduct menuProduct : menuProducts) {
            Product product = menuProduct.findProduct(products);
            sum = sum.add(menuProduct.calculatePrice(product));
        }
        price.compareToSum(sum);
    }

    public void addMenuIdToMenuProducts() {
        this.menuProducts = menuProducts.stream()
                .map(menuProduct -> menuProduct.addMenuId(id))
                .collect(Collectors.toList());
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

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public List<Long> getProductIds() {
        return menuProducts.stream()
                .map(MenuProduct::getProductId)
                .collect(Collectors.toList());
    }
}
