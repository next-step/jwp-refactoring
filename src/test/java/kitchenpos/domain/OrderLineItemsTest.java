package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.exception.KitchenposException;

class OrderLineItemsTest {

    @DisplayName("사이즈 값이 맞는지 확인")
    @Test
    void validateSize() {
        OrderLineItemRequest orderLineItem = new OrderLineItemRequest(1L, 1);
        OrderLineItems orderLineItems = new OrderLineItems(Collections.singletonList(orderLineItem));

        assertThatExceptionOfType(KitchenposException.class)
            .isThrownBy(() -> orderLineItems.validateSize(2));
    }

    @DisplayName("비어 있는 상품 목록으로 생성시 에러")
    @Test
    void constructFail() {
        assertThatExceptionOfType(KitchenposException.class)
            .isThrownBy(() -> new OrderLineItems(Collections.emptyList()));
    }
}