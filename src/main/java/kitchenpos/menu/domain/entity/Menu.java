package kitchenpos.menu.domain.entity;

import kitchenpos.menu.domain.value.MenuProducts;
import kitchenpos.menu.domain.value.Price;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Embedded
    private Price price;

    @ManyToOne
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts;

    public Menu() {
    }

    public Menu(String name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
        this.menuProducts.toMenu(this);
    }

    public static Menu of(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        System.out.println("name = " + name);
        System.out.println("menuGroup = " + menuGroup);
        System.out.println("price = " + price);
        System.out.println("menuProducts = " + menuProducts);
        validateMenuProductsSum(price, menuProducts);
        return new Menu(name, Price.of(price), menuGroup, new MenuProducts(menuProducts));
    }

    private static void validateMenuProductsSum(BigDecimal menuPrice, List<MenuProduct> menuProducts) {
        Double sum = menuProducts.stream()
                .mapToDouble(menuProduct -> menuProduct.getProduct().price())
                .sum();
        System.out.println("sum = " + sum);
        System.out.println("menuPrice = " + menuPrice);
//        if (sum.compareTo(menuPrice.doubleValue()) > 0 ) {
//            throw new IllegalArgumentException();
//        }
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

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getMenuProducts();
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }
}
