package kitchenpos.order.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static kitchenpos.order.domain.Order.OrderStatus.*;
import static kitchenpos.utils.TestUtils.getRandomId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("주문")
class OrderTest {

    @Test
    @DisplayName("주문를 생성한다. 주문 생성시 주문상태는 [조리] 상태이다.")
    public void initOrder() {
        // when
        Order order = 주문_생성();

        // then
        assertThat(order).isNotNull();
        assertThat(order.equalsByOrderStatus(COOKING)).isTrue();
    }

    @ParameterizedTest
    @DisplayName("주문의 상태를 변경한다.")
    @EnumSource(value = Order.OrderStatus.class, names = {"COOKING", "MEAL", "COMPLETION"})
    public void changeOrderStatus(Order.OrderStatus orderStatus) {
        // given
        Order order = 주문_생성();

        // when
        order.changeOrderStatus(orderStatus);

        // then
        assertThat(order.equalsByOrderStatus(orderStatus)).isTrue();
    }

    @ParameterizedTest
    @DisplayName("완료된 주문의 상태는 변경할수 없다.")
    @EnumSource(value = Order.OrderStatus.class, names = {"COOKING", "MEAL", "COMPLETION"})
    public void exceptionchangeOrderStatus(Order.OrderStatus orderStatus) {
        // given
        Order order = 주문_생성();
        order.changeOrderStatus(COMPLETION);

        // then
        assertThatThrownBy(() -> order.changeOrderStatus(orderStatus))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(String.format("%s의 상태는 변경 불가능합니다.", COMPLETION.remark()));
    }

    private Order 주문_생성() {
        Long orderTableId = getRandomId();
        Long menuId = getRandomId();
        OrderLineItem orderLineItem = OrderLineItem.valueOf(menuId, 1);

        return new Order(getRandomId(), orderTableId, orderLineItem);
    }

}