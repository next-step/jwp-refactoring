package domain.product;

import api.product.domain.Product;

import java.math.BigDecimal;

/**
 * packageName : kitchenpos.fixtures
 * fileName : ProductFixtures
 * author : haedoang
 * date : 2021/12/17
 * description :
 */
public class ProductFixtures {
    public static Product 양념치킨() {
        return new Product("양념치킨", new BigDecimal(16000));
    }

    public static Product 후라이드() {
        return new Product("후라이드", new BigDecimal(16000));
    }
}
