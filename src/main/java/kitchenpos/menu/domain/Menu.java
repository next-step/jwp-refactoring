package kitchenpos.menu.domain;

import kitchenpos.common.domain.BaseEntity;
import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Menu extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Price price;
    private Long menuGroupId;

    @OneToMany(
            mappedBy = "menu",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }


    public Menu(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this.name = name;
        this.price = new Price(price);
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = new Price(price);
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    private Menu(String name, BigDecimal price, Long menuGroupId) {
        this.name = name;
        this.price = new Price(price);
        this.menuGroupId = menuGroupId;
    }

    public static Menu createMenu(String name, Long menuGroupId, BigDecimal price, List<MenuProduct> menuProducts) {
        validate(price, menuProducts);

        Menu menu = new Menu(name, price, menuGroupId);

        menuProducts.forEach(menu::addMenuProduct);

        return menu;
    }

    private void addMenuProduct(MenuProduct menuProduct) {
        this.menuProducts.add(menuProduct);
        menuProduct.setMenu(this);
    }

    private static void validate(BigDecimal price, List<MenuProduct> menuProducts) {
        validateEmptyMenuProducts(menuProducts);
        validatePrice(price, menuProducts);
    }

    private static void validatePrice(BigDecimal price, List<MenuProduct> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = menuProduct.getProduct();
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }

    private static void validateEmptyMenuProducts(List<MenuProduct> menuProducts) {
        if (CollectionUtils.isEmpty(menuProducts)) {
            throw new IllegalArgumentException();
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
