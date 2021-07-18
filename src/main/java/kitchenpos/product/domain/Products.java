package kitchenpos.product.domain;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Products {
    private List<Product> products;

    public Products(List<Product> products) {
        this.products = products;
    }

    public boolean contains(Product product) {
        return this.products.contains(product);
    }

    public int productSize() {
        return products.size();
    }

    public BigDecimal calcTotalProductAmount(Map<Long, Long> menuProductMap) {
        return products.stream().filter(product -> menuProductMap.containsKey(product.getId()))
                .map(product -> product.multiplyQuantity(menuProductMap.get(product.getId())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Products products1 = (Products) object;
        return Objects.equals(products, products1.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(products);
    }
}
