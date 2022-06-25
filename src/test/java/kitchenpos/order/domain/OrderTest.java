package kitchenpos.order.domain;

import static kitchenpos.helper.OrderFixtures.주문_만들기;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.time.LocalDateTime;
import kitchenpos.order.consts.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 관련 Domain 단위 테스트")
class OrderTest {

    @DisplayName("주문 테이블 상태 일치 여부를 확인한다.")
    @Test
    void checkChangeEmpty() {
        //given
        Order order1 = new Order(null, OrderStatus.MEAL, LocalDateTime.now());
        Order order2 = new Order(null, OrderStatus.COOKING, LocalDateTime.now());
        Order order3 = new Order(null, OrderStatus.COMPLETION, LocalDateTime.now());

        //when then
        assertThatIllegalStateException()
                .isThrownBy(order1::checkPossibleChangeEmpty);
        assertThatIllegalStateException()
                .isThrownBy(order2::checkPossibleChangeEmpty);
        assertThatNoException()
                .isThrownBy(order3::checkPossibleChangeEmpty);
    }

    @DisplayName("주문 상태가 조리, 식사 인 경우 단체 지정 해제 할 수 없다.")
    @Test
    void checkPossibleUngroupingOrderStatus() {
        //given
        Order order1 = new Order(null, OrderStatus.MEAL, LocalDateTime.now());
        Order order2 = new Order(null, OrderStatus.COOKING, LocalDateTime.now());
        Order order3 = new Order(null, OrderStatus.COMPLETION, LocalDateTime.now());

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(order1::checkPossibleUngroupingOrderStatus);
        assertThatIllegalArgumentException()
                .isThrownBy(order2::checkPossibleUngroupingOrderStatus);
        assertThatNoException()
                .isThrownBy(order3::checkPossibleUngroupingOrderStatus);
    }


    @DisplayName("주문 상태를 업데이트 한다.")
    @Test
    void changeOrderStatus() {
        //given
        Order order = 주문_만들기(1L, OrderStatus.COOKING, null);

        //when
        order.changeOrderStatus(OrderStatus.MEAL);

        //then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @DisplayName("주문 상태가 계산완료인 경우 업데이트 할 수 없다.")
    @Test
    void changeOrderStatus_completion() {
        //given
        Order order = 주문_만들기(1L, OrderStatus.COMPLETION, null);

        //when
        assertThatIllegalArgumentException()
                .isThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL));
    }
}
