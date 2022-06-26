package kitchenpos.order.domain;

import static kitchenpos.helper.MenuFixtures.메뉴_만들기;
import static kitchenpos.helper.OrderFixtures.주문_만들기;
import static kitchenpos.helper.OrderLineItemFixtures.주문_항목_만들기;
import static kitchenpos.helper.TableFixtures.빈_테이블_만들기;
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

    @DisplayName("빈테이블인 경우 주문을 등록 할 수 없다.")
    @Test
    void order_empty_table() {
        //given
        OrderTable emptyTable = 빈_테이블_만들기();
        OrderLineItem orderLineItem = 주문_항목_만들기(메뉴_만들기("닭", 0), 1);
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.add(orderLineItem);

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Order(OrderStatus.COMPLETION, orderLineItems, LocalDateTime.now(), emptyTable))
                .withMessageContaining("빈테이블인 경우 주문을 등록 할 수 없습니다.");
    }

    @DisplayName("주문 항목이 없는 경우 주문을 등록 할 수 없다.")
    @Test
    void order_not_item() {
        //given
        OrderTable orderTable = 주문_테이블_만들기();

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Order(OrderStatus.COMPLETION, new OrderLineItems(), LocalDateTime.now(), orderTable))
                .withMessageContaining("주문 항목이 없는 경우 주문을 등록 할 수 없습니다.");
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
