package kitchenpos.domain;

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

    @ManyToOne(fetch = FetchType.LAZY)
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {
    }

    private Menu(String name, Price price, MenuGroup menuGroup, List<MenuProduct> menuProduct) {
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = new MenuProducts(menuProduct);
    }


    public static Menu of(String name, Price price, MenuGroup menuGroup, List<MenuProduct> menuProduct) {
        return new Menu(name, price, menuGroup, menuProduct);
    }

    private static void validCheckMeuProductPrice(List<MenuProduct> menuProducts, Price price) {
        BigDecimal sum = BigDecimal.ZERO;

        for (MenuProduct menuProduct : menuProducts) {
            sum = sum.add(menuProduct.getProduct()
                    .getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        validCheckMenuPrice(sum, price);
    }

    private static void validCheckMenuPrice(BigDecimal sumPrice, Price requestPrice) {
        if (requestPrice.getPrice().compareTo(sumPrice) > 0) {
            throw new IllegalArgumentException();
        }
    }

    public void addMenuProducts(MenuProducts menuProducts) {
        validCheckMeuProductPrice(menuProducts.getList(), price);
        this.menuProducts = menuProducts;
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

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getList();
    }
}
