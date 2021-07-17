package kitchenpos.product.dto;

import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("상품 반환 테스트")
class ProductResponseTest {

    @Test
    void 상품_도메인_객체를_사용하여_상품_반환_객체_생성() {
        Product product = new Product(1L, "짜장면", new BigDecimal(7000));
        ProductResponse response = ProductResponse.of(product);
        assertThat(response.getId()).isEqualTo(product.getId());
        assertThat(response.getName()).isEqualTo(product.getName());
        assertThat(response.getPrice()).isEqualTo(product.getPrice().price());
    }
}
