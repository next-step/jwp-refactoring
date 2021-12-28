package kitchenpos.tobe.orders.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.util.List;
import kitchenpos.tobe.fixture.OrderLineItemFixture;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderLineItemsTest {

    @DisplayName("주문 항목 일급 컬렉션을 생성할 수 있다.")
    @Test
    void create() {
        // when
        final ThrowableAssert.ThrowingCallable request = () -> OrderLineItemFixture.ofList(
            OrderLineItemFixture.of(1L, 1L, 1L),
            OrderLineItemFixture.of(2L, 2L, 1L)
        );

        // then
        assertThatNoException().isThrownBy(request);
    }

    @DisplayName("주문 항목들에 포함된 메뉴 ID를 가져올 수 있다.")
    @Test
    void getMenuIds() {
        // given
        final OrderLineItems items = OrderLineItemFixture.ofList(
            OrderLineItemFixture.of(1L, 1L, 1L),
            OrderLineItemFixture.of(2L, 2L, 1L),
            OrderLineItemFixture.of(3L, 2L, 1L)
        );

        // when
        final List<Long> menuIds = items.getMenuIds();

        // then
        assertThat(menuIds.size()).isEqualTo(2);
    }
}
