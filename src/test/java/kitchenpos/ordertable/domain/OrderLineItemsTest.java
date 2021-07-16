package kitchenpos.ordertable.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItems;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static kitchenpos.common.Message.ERROR_ORDER_LINE_ITEMS_SHOULD_HAVE_AT_LEAST_ONE_ITEM;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderLineItemsTest {

    @DisplayName("주문항목의 개수가 1개 미만인 경우, 예외발생")
    @Test
    void 주문항목_개수_1개미만_예외발생() {
        assertThatThrownBy(() -> new OrderLineItems(Collections.EMPTY_LIST, new Order()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ERROR_ORDER_LINE_ITEMS_SHOULD_HAVE_AT_LEAST_ONE_ITEM.showText());
    }
}
