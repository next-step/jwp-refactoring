package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.mock;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import kitchenpos.table.domain.Headcount;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("주문")
class OrderTest {

    @Test
    @DisplayName("생성")
    void instance() {
        assertThatNoException()
            .isThrownBy(() -> Order.of(OrderTable.of(Headcount.from(5), TableStatus.FULL),
                Collections.singletonList(OrderLineItem.of(1L, 1L))));
    }

    @ParameterizedTest(name = "[{index}] {argumentsWithNames} 으로 생성 불가능")
    @DisplayName("주문 테이블과 주문 항목들은 필수")
    @MethodSource
    void instance_nullOrderTableOrOrderLineItem_thrownIllegalArgumentException(
        OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> Order.of(orderTable, orderLineItems))
            .withMessageEndingWith("필수입니다.");
    }

    private static Stream<Arguments> instance_nullOrderTableOrOrderLineItem_thrownIllegalArgumentException() {
        return Stream.of(
            Arguments.of(null, Collections.singletonList(mock(OrderLineItem.class))),
            Arguments.of(OrderTable.of(Headcount.from(5), TableStatus.FULL), null)
        );
    }
}
