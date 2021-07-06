package kitchenpos.domain.menu;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static javax.persistence.FetchType.LAZY;

@Embeddable
public class MenuProducts {

    @OneToMany(fetch = LAZY, mappedBy = "menu")
    private List<MenuProduct> value;

    public static MenuProducts of(List<MenuProduct> menuProducts) {
        return new MenuProducts(menuProducts);
    }

    private MenuProducts(List<MenuProduct> value) {
        this.value = value;
    }

    public MenuProducts() {
    }

    public List<MenuProduct> getValue() {
        return Collections.unmodifiableList(value);
    }

    public Price sumOfMenuProductPrice() {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : value) {
            final Product product = menuProduct.getProduct();
            Price multiply = product.multiply(BigDecimal.valueOf(menuProduct.getQuantity()));
            sum = sum.add(multiply.value);
        }
        return Price.of(sum);
    }

    protected void addMenuProduct(MenuProduct menuProduct){
        value.add(menuProduct);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuProducts that = (MenuProducts) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "MenuProducts{" +
                "menuProducts=" + value +
                '}';
    }
}
