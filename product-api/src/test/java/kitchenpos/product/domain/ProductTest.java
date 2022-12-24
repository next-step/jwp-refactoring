package kitchenpos.product.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ProductTest {

    @DisplayName("상품 생성 작업을 성공한다.")
    @Test
    void of() {
        // given
        String expectedName = "짜장면";
        BigDecimal expectedPrice = BigDecimal.valueOf(8000);

        // when
        Product 짜장면 = Product.of(expectedName, expectedPrice);

        // then
        assertAll(
                () -> assertThat(짜장면).isNotNull(),
                () -> assertThat(짜장면.getName()).isEqualTo(expectedName),
                () -> assertThat(짜장면.getPrice().value()).isEqualTo(expectedPrice)
        );
    }
}
