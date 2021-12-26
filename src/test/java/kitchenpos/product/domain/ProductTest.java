package kitchenpos.product.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("상품 도메인 테스트")
class ProductTest {
    private Product 짜장면;
    private Product 짬뽕;

    @BeforeEach
    void setUp() {
        짜장면 = new Product(1L, "짜장면", 3500);
        짬뽕 = new Product(2L, "짬뽕", 1500);
    }

    @DisplayName("상품을 등록할 수 있다.")
    @Test
    void createProductTest() {
        assertAll(
                () -> assertThat(짜장면.getName()).isEqualTo("짜장면"),
                () -> assertThat(짜장면.getPrice()).isEqualTo(BigDecimal.valueOf(3500))
        );
    }
}