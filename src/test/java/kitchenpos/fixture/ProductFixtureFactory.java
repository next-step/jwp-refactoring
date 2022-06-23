package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFixtureFactory {
    private ProductFixtureFactory() {
    }

    public static Product createProduct(String name, int price){
        Product product = new Product();
        product.setName(name);
        product.setPrice(new BigDecimal(price));
        return product;
    }

    public static Product createProduct(String name, long price){
        Product product = new Product();
        product.setName(name);
        product.setPrice(new BigDecimal(price));
        return product;
    }
}
