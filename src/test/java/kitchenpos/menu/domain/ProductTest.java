package kitchenpos.menu.domain;

import kitchenpos.exception.ProductErrorMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("상품 단위 테스트")
public class ProductTest {

    @DisplayName("이름 가격이 동일하면 상품은 동일하다.")
    @Test
    void 이름_가격이_동일하면_상품은_동일하다() {
        assertEquals(
                new Product("후라이드치킨", new BigDecimal(18000)),
                new Product("후라이드치킨", new BigDecimal(18000))
        );
    }

    @DisplayName("이름이 null이거나 빈값이면 상품을 생성할 수 없다.")
    @ParameterizedTest
    @NullAndEmptySource
    void 이름이_null이거나_빈값이면_상품을_생성할_수_없다(String name) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Product(name, new BigDecimal(18000)))
                .withMessage(ProductErrorMessage.REQUIRED_NAME.getMessage());
    }

    @DisplayName("상품의 총 가격을 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {"2:5000", "5:10000", "10:1000"}, delimiter = ':')
    void 상품의_총_가격을_계산한다(int quantity, int price) {
        Product product = new Product("후라이드치킨", new BigDecimal(price));
        BigDecimal expected = new BigDecimal(price).multiply(BigDecimal.valueOf(quantity));
        assertEquals(expected, product.calculateAmount(quantity));
    }
}
