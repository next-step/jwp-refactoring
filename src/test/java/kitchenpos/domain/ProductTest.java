package kitchenpos.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("상품 도메인 테스트")
class ProductTest {
    private Product 상품1;

    @BeforeEach
    void setUp() {
        상품1 = Product.of(1L, "상품1", 10000);
    }

    @DisplayName("상품을 등록할 수 있다.")
    @Test
    void createProductTest() {
        assertAll(
                () -> assertThat(상품1.getName()).isEqualTo("상품1"),
                () -> assertThat(상품1.getPrice()).isEqualTo(Price.from(new BigDecimal("10000")))
        );
    }
}
