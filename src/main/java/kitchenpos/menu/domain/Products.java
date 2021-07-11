package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Products {

    @OneToMany(mappedBy = "menuProduct")
    private List<Product> products;

    public Products() {
    }

    public Products(final List<Product> productList) {
        this.products = productList;
    }

    public BigDecimal calculatePrice(final Long productId, final long quantity) {
        return products.stream()
            .filter(product -> Objects.equals(product.getId(), productId))
            .findFirst()
            .map(product -> product.getPrice().multiply(new BigDecimal(quantity)))
            .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final Products products1 = (Products)o;
        return Objects.equals(products, products1.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(products);
    }
}
