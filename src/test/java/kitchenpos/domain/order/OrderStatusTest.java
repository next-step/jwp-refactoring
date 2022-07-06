package kitchenpos.domain.order;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Domain:OrderStatus")
class OrderStatusTest {

    @Test
    @DisplayName("주문 테이블/단체지정 해제 시, 상태 변경이 불가능한 주문 상태 목록 조회")
    void cnaNotChangeOrderTableStatuses() {
        // When
        List<OrderStatus> actual = OrderStatus.canNotChangeOrderTableStatuses();

        // Then
        assertThat(actual).containsOnly(OrderStatus.MEAL, OrderStatus.COOKING);
    }

    @ParameterizedTest(name = "case[{index}] : {0} => {1}")
    @MethodSource
    @DisplayName("현재 주문상태가 완료 상태인지 여부 반환")
    public void isCompletion(final OrderStatus given, final boolean expected) {
        // When & Then
        assertThat(given.isCompletion()).isEqualTo(expected);
    }

    private static Stream<Arguments> isCompletion() {
        return Stream.of(
            Arguments.of(OrderStatus.COOKING, false),
            Arguments.of(OrderStatus.MEAL, false),
            Arguments.of(OrderStatus.COMPLETION, true)
        );
    }
}
