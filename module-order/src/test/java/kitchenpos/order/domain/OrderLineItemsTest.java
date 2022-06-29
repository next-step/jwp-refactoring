package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.common.collect.Lists;
import java.util.Collections;
import kitchenpos.order.exception.EmptyOrderLineItemsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("OrderLineItems 주문 항목 일급 컬랙션 도메인 테스트")
class OrderLineItemsTest {

    @DisplayName("주문 항목들을 생성할 수 있다.")
    @Test
    void create01() {
        // given
        OrderLineItem orderLineItem1 = OrderLineItem.of(1L, 1L);
        OrderLineItem orderLineItem2 = OrderLineItem.of(1L, 1L);

        // when
        OrderLineItems orderLineItems = OrderLineItems.from(Lists.newArrayList(orderLineItem1, orderLineItem2));

        // then
        assertAll(
                () -> assertThat(orderLineItems).isNotNull(),
                () -> assertThat(orderLineItems.getReadOnlyValues()).containsExactly(orderLineItem1, orderLineItem2)
        );
    }
    @DisplayName("주문 항목들을 생성할 수 없다.")
    @Test
    void create02() {
        // given & when & when
        assertThrows(EmptyOrderLineItemsException.class, () -> OrderLineItems.from(Collections.emptyList()));
    }
}
