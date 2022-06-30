package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.exception.NotExistException;
import kitchenpos.product.domain.Product;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts(){
    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public void add(MenuProduct menuProduct) {
        this.menuProducts.add(menuProduct);
    }

    public List<MenuProduct> get() {
        return menuProducts;
    }

    public Price totalPrice(List<Product> products) {
        Price total = Price.zero();
        for (MenuProduct menuProduct : menuProducts) {
            final Product product = findSameProduct(products, menuProduct);
            total = total.sum(menuProduct.price(product.getPrice()));
        }
        return total;
    }

    private Product findSameProduct(List<Product> products, MenuProduct menuProduct) {
        return products.stream()
                .filter(it -> menuProduct.sameProductId(it.getId()))
                .findFirst()
                .orElseThrow(NotExistException::new);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuProducts that = (MenuProducts) o;
        return Objects.equals(menuProducts, that.menuProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuProducts);
    }
}
