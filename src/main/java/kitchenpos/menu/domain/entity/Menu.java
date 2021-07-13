package kitchenpos.menu.domain.entity;

import kitchenpos.menu.domain.value.MenuProducts;
import kitchenpos.menu.domain.value.Price;

import javax.persistence.*;
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

    public static Menu of(String name, Price price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        validateMenuProductsSum(price, menuProducts);
        return new Menu(name, price, menuGroup, new MenuProducts(menuProducts));
    }

    private static void validateMenuProductsSum(Price menuPrice, List<MenuProduct> menuProducts) {
        Double sum = menuProducts.stream()
                .mapToDouble(menuProduct -> menuProduct.getProduct().price())
                .sum();
        if(menuPrice.greaterThan(sum)){
            throw new IllegalArgumentException();
        }
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
