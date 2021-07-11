package kitchenposNew.menu.domain;

import kitchenposNew.menu.domain.MenuProduct;
import kitchenposNew.wrap.Price;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Embedded
    private Price price;

    private Long menuGroupId;

    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts;

    protected Menu() {
    }

    public Menu(Long id, String name, Price price, Long menuGroupId, List<MenuProduct> menuProducts) {
        priceValidate(price, menuProducts);
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
        registerProduct();
    }

    public Menu(String name, Price price, Long menuGroupId, List<MenuProduct> menuProducts) {
        priceValidate(price, menuProducts);
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
        registerProduct();
    }

    private void registerProduct() {
        menuProducts.forEach(
                menuProduct -> menuProduct.registerMenu(this)
        );
    }

    private void priceValidate(Price price, List<MenuProduct> menuProducts) {
        BigDecimal productsPrice = BigDecimal.ZERO;
        for (MenuProduct menuProduct: menuProducts) {
            productsPrice = productsPrice.add(menuProduct.getPriceByQuantity());
        }
        if (!price.isCheapThanProductsPrice(productsPrice)) {
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
        return this.menuProducts;
    }

    public Long getMenuGroupId() {
        return this.menuGroupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return Objects.equals(id, menu.id) && Objects.equals(name, menu.name) && Objects.equals(price, menu.price) && Objects.equals(menuGroupId, menu.menuGroupId) && Objects.equals(menuProducts, menu.menuProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, menuGroupId, menuProducts);
    }
}
