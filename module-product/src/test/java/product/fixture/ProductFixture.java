package product.fixture;

import java.math.BigDecimal;
import product.domain.Product;
import product.dto.ProductRequestDto;
import product.dto.ProductResponseDto;

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
