package kitchenpos.order.domain;

import kitchenpos.ordertable.domain.OrderTable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
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
    @DisplayName("1 개 이상의 등록된 메뉴로 주문을 등록할 수 있다.")
    public void createFail1() throws Exception {
        // given
        OrderTable orderTable = new OrderTable(0, false);

        // when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Order.of(orderTable, new OrderLineItems(new ArrayList<>())))
                .withMessageMatching("최소 주문 메뉴 개수를 만족하지 못하여 주문을 등록할 수 없습니다.");
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

    @Test
    @DisplayName("주문 상태를 변경할 수 있다.")
    public void changeOrderStatus() throws Exception {
        // given
        OrderTable orderTable = new OrderTable(0, false);
        OrderLineItem orderLineItem = new OrderLineItem();
        OrderLineItems orderLineItems = new OrderLineItems(Collections.singletonList(orderLineItem));
        Order order = Order.of(orderTable, orderLineItems);

        // when
        order.changeOrderStatus(OrderStatus.MEAL);
        // then
        Assertions.assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL);

        // when
        order.changeOrderStatus(OrderStatus.COMPLETION);
        // then
        Assertions.assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
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
