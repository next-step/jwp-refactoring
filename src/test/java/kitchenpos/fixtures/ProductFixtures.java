package kitchenpos.fixtures;

import kitchenpos.domain.Product;

import java.math.BigDecimal;

/**
 * packageName : kitchenpos.fixtures
 * fileName : ProductFixtures
 * author : haedoang
 * date : 2021/12/17
 * description :
 */
public class ProductFixtures {
    public static Product createProduct(Long id, String name, BigDecimal price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        return product;
    }
}
