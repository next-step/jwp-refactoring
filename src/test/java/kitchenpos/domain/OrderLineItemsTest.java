package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderLineItemsTest {

    @Test
    void 주문_항목이_있는지_확인한다() {
        // when & then
        assertThatThrownBy(() ->
                new OrderLineItems(new ArrayList<>())
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 항목이 없습니다.");
    }

}
