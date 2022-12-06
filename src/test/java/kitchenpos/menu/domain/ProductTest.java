package kitchenpos.menu.domain;

import kitchenpos.menu.exception.ProductExceptionCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("상품 클래스 테스트")
class ProductTest {
    @Test
    void 동등성_테스트() {
        assertEquals(new Product("스파게티", new BigDecimal(18000)),
                new Product("스파게티", new BigDecimal(18000)));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 이름이_null이거나_빈_문자열인_상품을_생성할_수_없음(String name) {
        assertThatThrownBy(() -> {
            new Product(name, new BigDecimal(18000));
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ProductExceptionCode.REQUIRED_NAME.getMessage());
    }
}
