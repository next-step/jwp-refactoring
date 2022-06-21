package kitchenpos.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrdersRequestTest {
    @Test
    @DisplayName("주문 항목이 비어있으면 주문에 실패한다.")
    void create_fail_1() {
        //given
        //then
        assertThatThrownBy(() -> new OrdersRequest(1L, Collections.emptyList())).isExactlyInstanceOf(
                IllegalArgumentException.class);
    }
}
