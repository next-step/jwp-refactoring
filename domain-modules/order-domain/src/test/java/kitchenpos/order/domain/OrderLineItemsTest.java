package kitchenpos.order.domain;

import static kitchenpos.common.exception.ExceptionMessage.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.exception.BadRequestException;

class OrderLineItemsTest {

    @DisplayName("주문 항목은 최소 1개 이상이어야 한다.")
    @Test
    void createOrderLineItemsEmpty() {
        List<OrderLineItem> emptyOrderLineItems = Collections.emptyList();
        assertThatThrownBy(() -> new OrderLineItems(emptyOrderLineItems))
            .isInstanceOf(BadRequestException.class)
            .hasMessage(WRONG_VALUE.getMessage());
    }
}
