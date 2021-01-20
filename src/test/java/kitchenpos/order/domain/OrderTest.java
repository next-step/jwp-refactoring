package kitchenpos.order.domain;

import kitchenpos.ordertable.domain.OrderTable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class OrderTest {

    @Test
    @DisplayName("주문을 등록할 수 있다.")
    public void create() throws Exception {
        // given
        OrderTable orderTable = new OrderTable(0, false);
        OrderLineItem orderLineItem = new OrderLineItem();
        OrderLineItems orderLineItems = new OrderLineItems(Collections.singletonList(orderLineItem));

        // when
        Order order = Order.of(orderTable, orderLineItems);

        // then
        Assertions.assertThat(order).isNotNull();
    }

    @Test
    @DisplayName("빈 테이블에는 주문을 등록할 수 없다.")
    public void createFail2() throws Exception {
        // given
        OrderTable orderTable = new OrderTable(0, true);
        OrderLineItem orderLineItem = new OrderLineItem();
        OrderLineItems orderLineItems = new OrderLineItems(Collections.singletonList(orderLineItem));

        // when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Order.of(orderTable, orderLineItems))
                .withMessageMatching("빈 테이블에는 주문을 등록할 수 없습니다.");
    }

    @DisplayName("주문 상태를 변경할 수 있다.")
    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"MEAL", "COMPLETION"})
    public void changeOrderStatus(OrderStatus orderStatus) throws Exception {
        // given
        OrderTable orderTable = new OrderTable(0, false);
        OrderLineItem orderLineItem = new OrderLineItem();
        OrderLineItems orderLineItems = new OrderLineItems(Collections.singletonList(orderLineItem));
        Order order = Order.of(orderTable, orderLineItems);

        // when
        order.changeOrderStatus(orderStatus);
        // then
        Assertions.assertThat(order.getOrderStatus()).isEqualTo(orderStatus);
    }

    @Test
    @DisplayName("주문 상태가 계산 완료인 경우 변경할 수 없다.")
    public void changeOrderStatusFail() throws Exception {
        // given
        OrderTable orderTable = new OrderTable(0, false);
        OrderLineItem orderLineItem = new OrderLineItem();
        OrderLineItems orderLineItems = new OrderLineItems(Collections.singletonList(orderLineItem));
        Order order = Order.of(orderTable, orderLineItems);
        order.changeOrderStatus(OrderStatus.COMPLETION);

        // when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL))
                .withMessageMatching("계산 완료 주문의 경우 상태를 변경할 수 없습니다.");
    }
}
