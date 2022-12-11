package kitchenpos.domain;

import java.util.Collections;
import java.util.List;
import kitchenpos.exception.EmptyOrderLineItemException;
import kitchenpos.exception.ExceptionMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 항목 일급 콜렉션 테스트")
class OrderLineItemsTest {

    @DisplayName("주문 항목 생성시 주문항목이 비어있으면 예외가 발생한다.")
    @Test
    void createException() {
        List<OrderLineItem> orderLineItems = Collections.emptyList();

        Assertions.assertThatThrownBy(() -> OrderLineItems.from(orderLineItems))
                .isInstanceOf(EmptyOrderLineItemException.class)
                .hasMessageStartingWith(ExceptionMessage.EMPTY_ORDER_LINE_ITEM);
    }
}
