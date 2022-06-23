package kitchenpos.fixture;

import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class ProductFixture {

    public static Product 상품_요청_데이터_생성(String name, BigDecimal price) {
        return new Product(null, name, price);
    }

    public static Product 상품_데이터_생성(Long id, String name, BigDecimal price) {
        return new Product(id, name, price);
    }

}
