package kitchenpos.order.domain;

import static kitchenpos.helper.OrderFixtures.주문_만들기;
import static kitchenpos.helper.OrderLineItemFixtures.주문_항목들_만들기;
import static kitchenpos.helper.TableFixtures.주문_테이블_만들기;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.time.LocalDateTime;
import kitchenpos.order.consts.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 관련 Domain 단위 테스트")
class OrderTest {

    @DisplayName("주문 항목이 없는 경우 주문을 등록 할 수 없다.")
    @Test
    void order_not_item() {
        //given
        OrderTable orderTable = 주문_테이블_만들기();
        Order order = new Order(LocalDateTime.now(), orderTable.getId(), 주문_항목들_만들기());

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> order.addOrderLineItem(new OrderLineItem()))
                .withMessageContaining("주문 항목이 없는 경우 주문에 등록 할 수 없습니다.");
        assertThatIllegalArgumentException()
                .isThrownBy(() -> order.addOrderLineItem(null))
                .withMessageContaining("주문 항목이 없는 경우 주문에 등록 할 수 없습니다.");
    }


    @DisplayName("주문 상태를 업데이트 한다.")
    @Test
    void changeOrderStatus() {
        //given
        Order order = 주문_만들기(OrderStatus.COOKING, 주문_테이블_만들기(), 주문_항목들_만들기());

        //when
        order.changeOrderStatus(OrderStatus.MEAL);

        //then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @DisplayName("주문 상태가 계산완료인 경우 업데이트 할 수 없다.")
    @Test
    void changeOrderStatus_completion() {
        //given
        Order order = 주문_만들기(1L, OrderStatus.COMPLETION, 주문_테이블_만들기(), 주문_항목들_만들기());

        //when
        assertThatIllegalArgumentException()
                .isThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL));
    }
}
