package kitchenpos.domain;

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

    public Products(List<Product> productList) {
        this.products = productList;
    }

    public BigDecimal calculatePrice(Long productId, long quantity) {
        return products.stream()
            .filter(product -> Objects.equals(product.getId(), productId))
            .findFirst()
            .map(product -> product.getPrice().multiply(new BigDecimal(quantity)))
            .orElseThrow(IllegalArgumentException::new);
    }
}
