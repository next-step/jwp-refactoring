package kitchenpos.tobe.orders.domain.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.tobe.common.domain.Validator;
import kitchenpos.tobe.fixture.OrderFixture;
import kitchenpos.tobe.fixture.OrderLineItemFixture;
import kitchenpos.tobe.orders.domain.order.Order;
import kitchenpos.tobe.orders.domain.order.OrderLineItems;
import kitchenpos.tobe.orders.domain.order.OrderStatus;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderTest {

    private Validator<Order> validator;

    @BeforeEach
    void setUp() {
        validator = new FakeOrderValidator();
    }

    @DisplayName("주문을 생성할 수 있다.")
    @Test
    void create() {
        // given
        final Long orderId = 1L;
        final Long orderTableId = 1L;
        final OrderLineItems items = OrderLineItemFixture.ofList(
            OrderLineItemFixture.of(1L, 1L, 1L),
            OrderLineItemFixture.of(2L, 2L, 1L)
        );

        // when
        final Order order = OrderFixture.of(orderId, orderTableId, items, validator);

        // then
        assertThat(order.getStatus()).isEqualTo(OrderStatus.COOKING);
    }

    @DisplayName("주문 상태를 변경할 수 있다.")
    @Test
    void changeStatus() {
        // given
        final Long orderId = 1L;
        final Long orderTableId = 1L;
        final OrderLineItems items = OrderLineItemFixture.ofList(
            OrderLineItemFixture.of(1L, 1L, 1L),
            OrderLineItemFixture.of(2L, 2L, 1L)
        );
        final Order order = OrderFixture.of(orderId, orderTableId, items, validator);

        // when
        final OrderStatus expectedStatus = OrderStatus.MEAL;
        order.changeStatus(expectedStatus);

        // then
        assertThat(order.getStatus()).isEqualTo(expectedStatus);
    }

    @DisplayName("이미 완료된 주문의 주문 상태를 변경할 수 없다.")
    @Test
    void changeStatusFailOrderCompleted() {
        // given
        final Long orderId = 1L;
        final Long orderTableId = 1L;
        final OrderLineItems items = OrderLineItemFixture.ofList(
            OrderLineItemFixture.of(1L, 1L, 1L),
            OrderLineItemFixture.of(2L, 2L, 1L)
        );
        final Order order = OrderFixture.of(orderId, orderTableId, items, validator);
        order.changeStatus(OrderStatus.COMPLETION);

        // when
        final ThrowableAssert.ThrowingCallable request = () ->
            order.changeStatus(OrderStatus.COMPLETION);

        // then
        assertThatThrownBy(request).isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("주문에 포함된 주문 항목들의 메뉴 ID를 가져올 수 있다.")
    @Test
    void getMenuIds() {
        // given
        final Long orderId = 1L;
        final Long orderTableId = 1L;
        final OrderLineItems items = OrderLineItemFixture.ofList(
            OrderLineItemFixture.of(1L, 1L, 1L),
            OrderLineItemFixture.of(2L, 2L, 1L),
            OrderLineItemFixture.of(3L, 2L, 1L)
        );
        final Order order = OrderFixture.of(orderId, orderTableId, items, validator);

        // when
        final List<Long> menuIds = order.getMenuIds();

        // then
        assertThat(menuIds.size()).isEqualTo(2);
    }
}
