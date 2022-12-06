package kitchenpos.menu.domain;

import kitchenpos.menu.exception.ProductExceptionCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("상품 가격 클래스 테스트")
class ProductPriceTest {
    @Test
    void 상품_가격은_null일_수_없음() {
        assertThatThrownBy(() -> {
            new ProductPrice(null);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ProductExceptionCode.REQUIRED_PRICE.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = { -1, -100, -500, -1000 })
    void 상품_가격은_0보다_작을_수_없음(int price) {
        assertThatThrownBy(() -> {
            new ProductPrice(new BigDecimal(price));
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ProductExceptionCode.INVALID_PRICE.getMessage());
    }

    @ParameterizedTest
    @CsvSource(value = { "2:5000", "3:6000", "7:10000", "10:1000" }, delimiter = ':')
    void 수량과_가격의_곱을_구함(int quantity, int price) {
        ProductPrice productPrice = new ProductPrice(new BigDecimal(price));
        assertEquals(new BigDecimal(quantity * price), productPrice.multiply(new BigDecimal(quantity)));
    }
}
