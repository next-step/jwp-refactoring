package kitchenpos.product.domain;

import kitchenpos.common.ErrorCode;
import kitchenpos.menu.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductTest {

    @DisplayName("이름이 같으면 동등하다.")
    @Test
    void equalsTest() {
        assertEquals(new Product("제육", new BigDecimal(8000)),
                new Product("제육", new BigDecimal(8000)));
    }

    @DisplayName("이름이 null이거나 빈 문자열인 상품을 생성할 수 없다.")
    @ParameterizedTest
    @NullAndEmptySource
    void makeExceptionWhenProductNameIsNull(String name) {
        assertThatThrownBy(() -> {
            new Product(name, new BigDecimal(18000));
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.INVALID_FORMAT_PRODUCT_NAME.getErrorMessage());
    }

    @DisplayName("메뉴의 최대 가격은 메뉴에 속한 총 가격의 합이다.")
    @ParameterizedTest
    @CsvSource(value = { "2:5000", "3:6000", "7:10000", "10:1000" }, delimiter = ':')
    void 상품의_총_금액을_구함(int quantity, int price) {
        Product product = new Product("동파육", new BigDecimal(price));
        assertEquals(new BigDecimal(quantity * price), product.calculateAmount(quantity));
    }
}
