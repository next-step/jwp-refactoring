package kitchenpos.menu.domain;

import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.group.domain.MenuGroup;
import kitchenpos.product.domain.Product;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
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
    private MenuProducts menuProducts;

    private Long menuGroupId;

    protected Menu() {

    }

    private Menu(String name, BigDecimal price) {
        this.name = name;
        this.menuPrice = new MenuPrice(price);
        this.menuProducts = new MenuProducts();
    }

    public static Menu create(String name, BigDecimal price) {
        return new Menu(name, price);
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

    private void validatePrice(MenuPrice otherMenuPrice) {
        if (menuPrice.notMatch(otherMenuPrice)) {
            throw new IllegalArgumentException();
        }
    }

    public void grouping(MenuGroup menuGroup) {
        this.menuGroupId = menuGroup.getId();
    }

    public void addAllProduct(List<Product> products, MenuRequest menuRequest) {
        BigDecimal sum = BigDecimal.ZERO;
        for(Product product : products)
        {
            MenuProductRequest menuProductRequest = menuRequest.find(product);
            addProduct(product, menuProductRequest.getQuantity());
            sum = sum.add(product.multiplyQuantity(new BigDecimal(menuProductRequest.getQuantity())));
        }

        validatePrice(new MenuPrice(sum));
    }

    public void addProduct(Product product, Integer quantity) {
        this.menuProducts.add(this, product, quantity);
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
