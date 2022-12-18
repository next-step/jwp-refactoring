package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import kitchenpos.table.domain.OrderTable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderTest {

    @Test
    @DisplayName("주문 생성 테스트")
    void createTest(){
        // given
        OrderTable orderTable = new OrderTable(true);
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1);
        OrderLineItem orderLineItem2 = new OrderLineItem(2L, 1);

        // when
        Order order = new Order(orderTable, Arrays.asList(
                orderLineItem1, orderLineItem2
        ));

        // then
        assertThat(order).isNotNull();
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
        assertThat(order.getOrderLineItems()).hasSize(2);
    }

    @Test
    @DisplayName("주문 상태 변경 테스트 - 식사중으로 변경")
    void orderStatusChangeTest1(){
        // given
        OrderTable orderTable = new OrderTable(true);
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1);
        OrderLineItem orderLineItem2 = new OrderLineItem(2L, 1);
        Order order = new Order(orderTable, Arrays.asList(
                orderLineItem1, orderLineItem2
        ));

        // when
        order.changeOrderStatus(OrderStatus.MEAL);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
        assertThat(order.isDinning()).isTrue();
    }

    @Test
    @DisplayName("주문 상태 변경 테스트 - 결재완료로 변경")
    void orderStatusChangeTest2(){
        // given
        OrderTable orderTable = new OrderTable(true);
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1);
        OrderLineItem orderLineItem2 = new OrderLineItem(2L, 1);
        Order order = new Order(orderTable, Arrays.asList(
                orderLineItem1, orderLineItem2
        ));

        // when
        order.changeOrderStatus(OrderStatus.COMPLETION);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
        assertThat(order.isDinning()).isFalse();
    }

    @Test
    @DisplayName("주문 상태 변경 실패 테스트 - 결재완료인 상태의 주문은 다른 상태로 변경 할 수 없습니다.")
    void orderStatusChangeTest3(){
        // given
        OrderTable orderTable = new OrderTable(true);
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1);
        OrderLineItem orderLineItem2 = new OrderLineItem(2L, 1);
        Order order = new Order(orderTable, Arrays.asList(
                orderLineItem1, orderLineItem2
        ));
        order.changeOrderStatus(OrderStatus.COMPLETION);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> order.changeOrderStatus(OrderStatus.MEAL)
        );
        assertThatIllegalArgumentException().isThrownBy(
                () -> order.changeOrderStatus(OrderStatus.COOKING)
        );
    }
}
