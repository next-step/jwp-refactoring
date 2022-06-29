package kitchenpos.common.fixture;

import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequestDto;
import kitchenpos.product.dto.ProductResponseDto;

import java.math.BigDecimal;

public class ProductFixture {

    public static ProductRequestDto 상품_요청_데이터_생성(String name, BigDecimal price) {
        return new ProductRequestDto(name, price);
    }

    public static Product 상품_데이터_생성(Long id, String name, BigDecimal price) {
        return new Product(id, name, price);
    }

    public static ProductResponseDto 상품_응답_데이터_생성(Long id, String name, BigDecimal price) {
        return new ProductResponseDto(id, name, price);
    }

}
