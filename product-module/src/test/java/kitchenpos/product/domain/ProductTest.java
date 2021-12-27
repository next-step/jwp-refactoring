package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import kitchenpos.product.domain.Product;

class ProductTest {
    @DisplayName("가격이 null이거나 음수일 때 예외 발생")
    @ParameterizedTest
    @MethodSource("provideInvalidPrice")
    void createByInvalidPrice(final BigDecimal price) {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> new Product("후라이드", price))
            .withMessageContaining("가격");
    }

    private static Stream<BigDecimal> provideInvalidPrice() {
        return Stream.of(null, new BigDecimal(-1));
    }
}
