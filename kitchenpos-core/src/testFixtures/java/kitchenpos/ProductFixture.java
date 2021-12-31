package kitchenpos;

import kitchenpos.core.product.domain.Product;

public class ProductFixture {
    private ProductFixture() {
    }

    public static Product getProduct(Long id, String name, int price) {
        return Product.generate(id, name, price);
    }
}
