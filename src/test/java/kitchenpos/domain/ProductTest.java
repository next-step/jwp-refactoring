package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ProductTest {

    @Test
    void of() {
        // when
        long expectedId = 1L;
        String expectedName = "짜장면";
        BigDecimal expectedPrice = BigDecimal.valueOf(8000);
        Product 짜장면 = Product.of(expectedId, expectedName, expectedPrice);

        // then
        assertAll(
                () -> assertThat(짜장면).isNotNull(),
                () -> assertThat(짜장면.getId()).isEqualTo(expectedId),
                () -> assertThat(짜장면.getName()).isEqualTo(expectedName),
                () -> assertThat(짜장면.getPrice()).isEqualTo(expectedPrice)
        );
    }
}
