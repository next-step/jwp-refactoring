package kitchenpos.domain.order;

import kitchenpos.domain.order.exceptions.OrderStatusNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderStatusTest {
    @DisplayName("이름에 맞는 오브젝트를 찾을 수 있다.")
    @ParameterizedTest
    @MethodSource("findOrderStatusTestResource")
    void findOrderStatusTest(String name, OrderStatus expected) {
        // when
        OrderStatus orderStatus = OrderStatus.find(name);

        // then
        assertThat(orderStatus).isEqualTo(expected);
    }
    public static Stream<Arguments> findOrderStatusTestResource() {
        return Stream.of(
                Arguments.of("MEAL", OrderStatus.MEAL),
                Arguments.of("COMPLETION", OrderStatus.COMPLETION),
                Arguments.of("COOKING", OrderStatus.COOKING)
        );
    }

    @DisplayName("존재하지 않는 주문 상태를 찾을 경우 예외 발생")
    @Test
    void findOrderStatusFailTest() {
        // given
        String invalidName = "meal";

        // when, then
        assertThatThrownBy(() -> OrderStatus.find(invalidName))
                .isInstanceOf(OrderStatusNotFoundException.class)
                .hasMessage("존재하지 않는 주문상태입니다.");
    }

    @DisplayName("주문 상태를 바꿀 수 있는 상태인지 알려줄 수 있다.")
    @ParameterizedTest
    @MethodSource("canChangeTestResource")
    void canChangeTest(OrderStatus orderStatus, boolean expected) {
        assertThat(orderStatus.canChange()).isEqualTo(expected);
    }
    public static Stream<Arguments> canChangeTestResource() {
        return Stream.of(
                Arguments.of(OrderStatus.MEAL, true),
                Arguments.of(OrderStatus.COOKING, true),
                Arguments.of(OrderStatus.COMPLETION, false)
        );
    }
}