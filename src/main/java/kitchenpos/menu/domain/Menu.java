package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.product.domain.Product;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts;

    protected Menu() {
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        validatePrice(price);
        MenuProducts products = convertToMenuProducts(menuProducts);

        validateMenuProductsPriceThanMenuPrice(price, products);
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = products;
    }

    public Menu(Long id, String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        validatePrice(price);
        MenuProducts products = convertToMenuProducts(menuProducts);

        validateMenuProductsPriceThanMenuPrice(price, products);
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = products;
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup, MenuProducts menuProducts) {
        this(null, name, price, menuGroup, menuProducts);
    }

    public Menu(Long id, String name, BigDecimal price, MenuGroup menuGroup, MenuProducts menuProducts) {
        validatePrice(price);
        validateMenuProductsPriceThanMenuPrice(price, menuProducts);

        this.id = id;
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

    public BigDecimal getPrice() {
        return price;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getMenuProducts();
    }

    private MenuProducts convertToMenuProducts(List<MenuProduct> menuProducts) {
        List<MenuProduct> products = new ArrayList<>();

        for(MenuProduct menuProduct: menuProducts) {
            products.add(new MenuProduct(this, menuProduct.getProduct(), menuProduct.getQuantity()));
        }

        return new MenuProducts(products);
    }

    private void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    private void validateMenuProductsPriceThanMenuPrice(BigDecimal price, MenuProducts menuProducts) {
        BigDecimal totalPrice = menuProducts.calculateTotalPrice();

        if (price.compareTo(totalPrice) > 0) {
            throw new IllegalArgumentException();
        }
    }
}
