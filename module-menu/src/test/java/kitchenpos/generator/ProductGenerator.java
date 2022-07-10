package kitchenpos.generator;

import kitchenpos.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductCreateRequest;

import java.math.BigDecimal;

public class ProductGenerator {

    private ProductGenerator() {}

    public static Product 상품_생성(String name, Price price) {
        return new Product(name, price);
    }

    public static ProductCreateRequest 상품_생성_요청(String name, int price) {
        return new ProductCreateRequest(name, new BigDecimal(price));
    }
}
