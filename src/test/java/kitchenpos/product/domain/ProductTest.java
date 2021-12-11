package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.math.BigDecimal;
import java.util.stream.Stream;
import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("상품")
class ProductTest {

    @Test
    @DisplayName("생성")
    void instance() {
        assertThatNoException()
            .isThrownBy(() -> Product.of(Name.from("후라이드치킨"), Price.from(BigDecimal.ONE)));
    }

    @ParameterizedTest(name = "[{index}] {argumentsWithNames} 으로 생성 불가능")
    @DisplayName("이름과 가격은 필수")
    @MethodSource
    void instance_nullNameOrPrice_thrownIllegalArgumentException(Name name, Price price) {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> Product.of(name, price))
            .withMessageEndingWith("필수입니다.");
    }

    private static Stream<Arguments> instance_nullNameOrPrice_thrownIllegalArgumentException() {
        return Stream.of(
            Arguments.of(null, Price.from(BigDecimal.ONE)),
            Arguments.of(Name.from("후라이드치킨"), null)
        );
    }
}
