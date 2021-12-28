package kitchenpos.menu.domain;

import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.group.domain.MenuGroup;
import kitchenpos.product.domain.Product;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Table(name = "menu")
@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 100)
    private String name;

    @Embedded
    private MenuPrice menuPrice;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();;

    private Long menuGroupId;

    protected Menu() {

    }

    private Menu(String name, BigDecimal price) {
        this.name = name;
        this.menuPrice = new MenuPrice(price);
    }

    public static Menu prepared(String name, BigDecimal price) {
        return new Menu(name.trim(), price);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getMenuPrice() {
        return menuPrice.getPrice();
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getMenuProducts();
    }

    public boolean matchId(Long menuId) {
        return Objects.nonNull(this.id) && this.id.equals(menuId);
    }

    public void validatePrice() {
        MenuPrice menuPrice = this.menuProducts.totalPrice();
        if (menuPrice.notMatch(menuPrice)) {
            throw new IllegalArgumentException();
        }
    }

    public void grouping(Long menuGroupId) {
        this.menuGroupId = menuGroupId;
    }

    public void addProducts(Map<Product, Long> menuProducts) {

        for(Map.Entry<Product, Long> entry : menuProducts.entrySet()) {
            addProduct(entry.getKey(), entry.getValue());
        }

        validatePrice();
    }

    private void addProduct(Product product, Long quantity) {
        this.menuProducts.add(product, quantity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return Objects.equals(name, menu.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
