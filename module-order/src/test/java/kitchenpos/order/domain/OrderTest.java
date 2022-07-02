package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.Arrays;
import kitchenpos.helper.OrderBuilder;
import kitchenpos.helper.OrderLineItemBuilder;
import kitchenpos.order.consts.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 관련 Domain 단위 테스트")
class OrderTest {

    @DisplayName("주문 상태를 업데이트 한다.")
    @Test
    void changeOrderStatus() {
        //given
        OrderLineItem orderLineItem = OrderLineItemBuilder.builder().price(1000).quantity(1).build();
        Order order = OrderBuilder.builder()
                .orderStatus(OrderStatus.COOKING)
                .orderLineItems(Arrays.asList(orderLineItem))
                .build();

        //when
        order.changeOrderStatus(OrderStatus.MEAL);

        //then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }


    @DisplayName("주문 상태가 계산완료인 경우 업데이트 할 수 없다.")
    @Test
    void changeOrderStatus_completion() {
        //given
        OrderLineItem orderLineItem = OrderLineItemBuilder.builder().price(1000).quantity(1).build();
        Order order = OrderBuilder.builder()
                .orderStatus(OrderStatus.COMPLETION)
                .orderLineItems(Arrays.asList(orderLineItem))
                .build();

        //when
        assertThatIllegalArgumentException()
                .isThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL))
                .withMessageContaining("계산완료상태에서 주문 상태를 변경할 수 없습니다.");
    }
}
