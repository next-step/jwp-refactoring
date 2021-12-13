package kitchenpos.order.domain;

import static kitchenpos.menu.sample.MenuSample.이십원_후라이드치킨_두마리세트;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.util.stream.Stream;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("주문 항목")
class OrderLineItemTest {

    @Test
    @DisplayName("생성")
    void instance() {
        assertThatNoException()
            .isThrownBy(() -> OrderLineItem.of(Quantity.from(1L), 이십원_후라이드치킨_두마리세트()));
    }

    @ParameterizedTest(name = "[{index}] {argumentsWithNames} 으로 생성 불가능")
    @DisplayName("수량과 메뉴는 필수")
    @MethodSource
    void instance_nullQuantityOrMenu_thrownIllegalArgumentException(Quantity quantity, Menu menu) {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> OrderLineItem.of(quantity, menu))
            .withMessageEndingWith("필수입니다.");
    }

    private static Stream<Arguments> instance_nullQuantityOrMenu_thrownIllegalArgumentException() {
        return Stream.of(
            Arguments.of(null, 이십원_후라이드치킨_두마리세트()),
            Arguments.of(Quantity.from(1L), null)
        );
    }
}
