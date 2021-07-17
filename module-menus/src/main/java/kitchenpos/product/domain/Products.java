package kitchenpos.product.domain;

import java.util.List;

public class Products {
    private List<Product> products;

    public static Products of(List<Product> products) {
        return new Products(products);
    }

    public Products(List<Product> products) {
        this.products = products;
    }

    public boolean isSatisfiedBy(List<ProductOption> productOptions) {
        if (products.size() != productOptions.size()) {
            return false;
        }

        return productOptions.stream().allMatch(this::isSatisfiedBy);
    }

    private boolean isSatisfiedBy(ProductOption productOption) {
        return products.stream()
            .anyMatch(product -> product.isSatisfiedBy(productOption));
    }
}
