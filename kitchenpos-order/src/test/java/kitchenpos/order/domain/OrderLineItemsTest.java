package kitchenpos.order.domain;

import static kitchenpos.order.domain.OrderLineItemTestFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.common.exception.InvalidParameterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

@DisplayName("주문 항목 묶음 테스트")
class OrderLineItemsTest {
    @Test
    @DisplayName("주문 항목 묶음 생성")
    void createOrderLineItems() {
        // when
        OrderLineItems actual = OrderLineItems.of(짜장_탕수육_주문_항목, 짬뽕2_탕수육_주문_항목);

        // then
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual).isInstanceOf(OrderLineItems.class)
        );
    }

    @Test
    @DisplayName("주문 항목 묶음 생성시 주문 항목은 비어있을 수 없다.")
    void createOrderLineItemsByEmptyList() {
        // when & then
        assertThatThrownBy(() -> OrderLineItems.from(Collections.emptyList()))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("주문 항목은 비어있을 수 없습니다.");
    }
}
