package kitchenpos.product.dto;

import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("상품 요청 테스트")
class ProductRequestTest {

    @Test
    void 상품_request_객체를_이용하여_Product_entity생성() {
        ProductRequest request = new ProductRequest("짜장면", new BigDecimal(7000));
        Product product = request.toProduct();
        assertThat(product.getName()).isEqualTo(request.getName());
        assertThat(product.getPrice().price()).isEqualTo(request.getPrice());
    }
}
