package kitchenpos.fixture;

import kitchenpos.product.domain.Product;

import java.math.BigDecimal;

public class TestProductFactory {

    public static Product create(String name, int price) {
        return create(null, name, price);
    }


    public static Product create(Long id, String name, int price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));
        return product;
    }
}
