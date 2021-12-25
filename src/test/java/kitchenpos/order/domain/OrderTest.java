package kitchenpos.order.domain;

import kitchenpos.common.domain.Quantity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("주문 테스트")
class OrderTest {

    private Long orderTableId;
    private LocalDateTime orderedTime;
    private Long menuId;
    private OrderLineItem orderLineItem;
    private OrderLineItems orderLineItems;

    @BeforeEach
    void setUp() {
        orderTableId = 1L;
        orderedTime = LocalDateTime.now();
        menuId = 1L;
        orderLineItem = OrderLineItem.of(menuId, Quantity.of(1));
        orderLineItems = OrderLineItems.of(Arrays.asList(orderLineItem));
    }

    @DisplayName("주문 생성 성공 테스트")
    @Test
    void instantiate_success() {
        // when
        Order order = Order.of(orderTableId, OrderStatus.COOKING, orderedTime, orderLineItems);

        // then
        assertAll(
                () -> assertThat(order).isNotNull()
                , () -> assertThat(order.getOrderTableId()).isEqualTo(orderTableId)
                , () -> assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING)
                , () -> assertThat(order.getOrderedTime()).isEqualTo(orderedTime)
                , () -> assertThat(order.getOrderLineItems()).isEqualTo(orderLineItems)
        );
    }

    @DisplayName("주문 상태 수정 성공 테스트")
    @Test
    void changeOrderStatus_success() {
        // given
        Order order = Order.of(orderTableId, OrderStatus.COOKING, orderedTime, orderLineItems);

        // when
        order.changeOrderStatus(OrderStatus.MEAL);

        // then
        assertAll(
                () -> assertThat(order).isNotNull()
                , () -> assertThat(order.getOrderTableId()).isEqualTo(orderTableId)
                , () -> assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL)
                , () -> assertThat(order.getOrderedTime()).isEqualTo(orderedTime)
                , () -> assertThat(order.getOrderLineItems()).isEqualTo(orderLineItems)
        );
    }

    @DisplayName("주문 상태 수정 실패 테스트 - 주문 상태가 completion인 경우 주문 상태 변경할 수 없음")
    @Test
    void changeOrderStatus_failure() {
        // given
        Order order = Order.of(orderTableId, OrderStatus.COMPLETION, orderedTime, orderLineItems);

        // when & then
        org.assertj.core.api.Assertions.assertThatIllegalArgumentException()
                .isThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL));
    }
}
