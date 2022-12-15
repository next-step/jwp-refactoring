package kitchenpos.menu.domain;

import kitchenpos.ExceptionMessage;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    @Embedded
    private Price price;
    @ManyToOne
    @JoinColumn(name = "menu_group_id", foreignKey = @ForeignKey(name = "fk_menu_menu_group"))
    private MenuGroup menuGroup;
    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {}

    public Menu(Long id, String name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        this(name, price, menuGroup, menuProducts);
        this.id = id;
    }

    public Menu(String name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        if (menuProducts.isEmpty()) {
            throw new IllegalArgumentException(ExceptionMessage.EMPTY_MENU_PRODUCTS.getMessage());
        }
        final Price productTotalSum = menuProducts.getProductPriceSum();
        if (!price.lessOrEqualThan(productTotalSum)) {
            throw new IllegalArgumentException(ExceptionMessage.MENU_PRICE_LESS_PRODUCT_PRICE_SUM.getMessage());
        }
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
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
    public MenuProducts getMenuProducts() {
        return menuProducts;
    }
}
