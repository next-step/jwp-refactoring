package kitchenpos.domain.product;

import kitchenpos.domain.product.exceptions.InvalidProductPriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductPriceTest {
    @DisplayName("가격이 없거나 0원 미만으로 오브젝트를 생성할 수 없다.")
    @ParameterizedTest
    @NullSource
    @MethodSource("createFailTestResource")
    void createFailTest(BigDecimal invalidPrice) {
        assertThatThrownBy(() -> new ProductPrice(invalidPrice))
                .isInstanceOf(InvalidProductPriceException.class)
                .hasMessage("상품의 가격은 반드시 있어야 하며, 0원 이상이어야 합니다.");
    }
    public static Stream<Arguments> createFailTestResource() {
        return Stream.of(
                Arguments.of(BigDecimal.valueOf(-1)),
                Arguments.of(BigDecimal.valueOf(-2))
        );
    }
}