package kitchenpos.order.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static kitchenpos.order.application.OrderCrudService.ORDERLINEITEMS_EMPTY_EXCEPTION_MESSAGE;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("주문")
class OrderTest {

    @DisplayName("주문 항목이 비어있을 수 없다.")
    @Test
    void constructor_fail_orderItem() {
        assertThatThrownBy(() -> new Order(1L, new ArrayList<>()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDERLINEITEMS_EMPTY_EXCEPTION_MESSAGE);
    }
}
