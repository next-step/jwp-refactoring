package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;

public class Menu {
    private Long id;
    private String name;
    private Price price;
    private Long menuGroupId;
    private List<MenuProduct> menuProducts;
    private Products products;

    public Menu() {
    }

    public Menu(String name, Price price, Long menuGroupId, List<MenuProduct> menuProducts, Products products) {
        this(name, price, menuGroupId, menuProducts);
        this.products = products;

        final BigDecimal price1 = menuProducts.stream()
            .map(menuProduct -> products.calculatePrice(menuProduct.getProductId(), menuProduct.getQuantity()))
            .reduce(BigDecimal::add)
            .orElseThrow(IllegalArgumentException::new);

        if (price.greaterThan(price1)) {
            throw new IllegalArgumentException();
        }
    }

    public Menu(String name, Price price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = new Price(price);
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public void setMenuGroupId(final Long menuGroupId) {
        this.menuGroupId = menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void setMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }
}
