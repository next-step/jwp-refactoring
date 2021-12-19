package kitchenpos.order.domain;

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

@DisplayName("주문 항목 메뉴")
class OrderLineItemMenuTest {

    @Test
    @DisplayName("생성")
    void instance() {
        assertThatNoException()
            .isThrownBy(() -> OrderLineItemMenu.of(1L,
                Name.from("후라이드치킨_두마리세트"),
                Price.from(BigDecimal.TEN)));
    }

    @ParameterizedTest(name = "[{index}] {argumentsWithNames}으로 생성 불가능")
    @MethodSource
    @DisplayName("이름, 가격은 필수")
    void instance_nullNameOrPrice_thrownIllegalArgumentException(Name name, Price price) {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> OrderLineItemMenu.of(1L, name, price))
            .withMessageEndingWith("필수입니다.");
    }

    private static Stream<Arguments> instance_nullNameOrPrice_thrownIllegalArgumentException() {
        return Stream.of(
            Arguments.of(null, Price.from(BigDecimal.TEN)),
            Arguments.of(Name.from("후라이드치킨_두마리세트"), null)
        );
    }
}
