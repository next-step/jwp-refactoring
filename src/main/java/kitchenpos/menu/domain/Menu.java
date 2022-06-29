package kitchenpos.menu.domain;

import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.product.domain.Product;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static javax.persistence.CascadeType.*;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Embedded
    private Name name;
    @Embedded
    private Price price;
    private Long menuGroupId;
    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    public Menu() {
    }

    public Menu(String name, BigDecimal price, Long menuGroupId) {
        this(null, new Name(name), new Price(price), menuGroupId, new MenuProducts());
    }

    public Menu(String name, Price price, Long menuGroupId, MenuProducts menuProducts) {
        this(null, new Name(name), price, menuGroupId, menuProducts);
    }

    public Menu(Long id, Name name, Price price, Long menuGroupId, MenuProducts menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public void addMenuProduct(MenuProduct menuProduct) {
        this.menuProducts.add(menuProduct);
        menuProduct.setMenu(this);
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }

    public void addMenuProducts(List<MenuProductRequest> menuProductRequests) {
        if (menuProductRequests == null) {
            return;
        }
        menuProductRequests.forEach(menuProductRequest ->
                    this.addMenuProduct(menuProductRequest.toMenuProduct(this)));
    }

    public List<Long> getProductIds() {
        return menuProducts.getProductIds();
    }

    public void validate(List<Product> products) {
        Price sum = sumProductsPrice(products);
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }

    private Price sumProductsPrice(List<Product> products) {
        Price sum = new Price();
        for (final Product product : products) {
            sum = sum.plus(product.getPrice().multiply(menuProducts.getMenuProductQuantity(product.getId())));
        }
        return sum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return Objects.equals(getId(), menu.getId()) && Objects.equals(getName(), menu.getName()) && Objects.equals(getPrice(), menu.getPrice()) && Objects.equals(getMenuGroupId(), menu.getMenuGroupId()) && Objects.equals(getMenuProducts(), menu.getMenuProducts());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getPrice(), getMenuGroupId(), getMenuProducts());
    }
}
