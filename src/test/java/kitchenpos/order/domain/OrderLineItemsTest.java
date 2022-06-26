package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class OrderLineItemsTest {

    @ParameterizedTest(name = "{0} -> {1}")
    @DisplayName("빈 주문 상품을 추가할 경우 에러를 던진다.")
    @MethodSource("providerAddOrderLineItemsCase")
    void addOrderLineItems(List<OrderLineItem> inputOrderLineItems, Class<? extends Exception> exception) {
        OrderLineItems orderLineItems = new OrderLineItems();
        assertThatExceptionOfType(exception)
            .isThrownBy(() -> orderLineItems.addOrderLineItems(inputOrderLineItems));
    }

    private static Stream<Arguments> providerAddOrderLineItemsCase() {
        return Stream.of(
            Arguments.of(null, NullPointerException.class),
            Arguments.of(Collections.emptyList(), IllegalArgumentException.class),
            Arguments.of(Collections.singletonList(null), NullPointerException.class)
        );
    }

}
