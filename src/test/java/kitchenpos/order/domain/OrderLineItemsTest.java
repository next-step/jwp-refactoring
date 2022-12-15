package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderLineItemsTest {

    @DisplayName("주문 항목이 비어있으면 주문 항목 집합 생성 시 에러가 발생한다.")
    @Test
    void validateOrderLineItemsNotEmptyException() {
        assertThatThrownBy(() -> OrderLineItems.from(Collections.emptyList()))
            .isInstanceOf(IllegalArgumentException.class);
    }
}