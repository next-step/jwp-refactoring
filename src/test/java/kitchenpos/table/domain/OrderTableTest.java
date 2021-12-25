package kitchenpos.table.domain;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.IllegalArgumentException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@DisplayName("주문 테이블 도메인 테스트")
public class OrderTableTest {
    @DisplayName("빈 테이블 여부를 변경한다.")
    @Test
    void 빈_테이블_여부_변경() {
        // given
        OrderTable orderTable = OrderTable.of(5, false);
        Order order = Order.of(orderTable);
        order.changeOrderStatus(OrderStatus.COMPLETION);

        // when
        orderTable.changeEmpty(true);

        // then
        assertThat(orderTable.isEmpty()).isTrue();
    }

    @DisplayName("완료되지 않은 주문이 있을 경우 빈 테이블 여부를 변경할 수 없다.")
    @Test
    void 미완료_주문_존재_빈_테이블_여부_변경_예외() {
        // given
        OrderTable orderTable = OrderTable.of(5, false);
        Order order = Order.of(orderTable);
        order.changeOrderStatus(OrderStatus.MEAL);

        // when
        Throwable thrown = catchThrowable(() -> orderTable.changeEmpty(true));

        // then
        assertThat(thrown).isInstanceOf(BadRequestException.class)
                .hasMessage("현재 테이블은 주문 완료 상태가 아니므로 빈 테이블 설정을 할 수 없습니다.");
    }

    @DisplayName("테이블 그룹이 존재할 경우 빈 테이블 여부를 변경할 수 없다.")
    @Test
    void 테이블_그룹_존재_주문_존재_빈_테이블_여부_변경_예외() {
        // given
        OrderTable orderTable = OrderTable.of(5, true);
        orderTable.addGroup(TableGroup.create());
        Order order = Order.of(orderTable);
        order.changeOrderStatus(OrderStatus.COMPLETION);


        // when
        Throwable thrown = catchThrowable(() -> orderTable.changeEmpty(true));

        // then
        assertThat(thrown).isInstanceOf(BadRequestException.class)
                .hasMessage("테이블 그룹이 존재하므로 빈 테이블 설정을 할 수 없습니다.");
    }

    @DisplayName("손님 수를 변경한다.")
    @Test
    void 손님_수_변경() {
        // given
        OrderTable orderTable = OrderTable.of(5, false);
        Order order = Order.of(orderTable);

        // when
        orderTable.changeNumberOfGuests(4);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(4);
    }

    @DisplayName("양수가 아닌 수로 손님 수를 변경할 수 없다.")
    @Test
    void 양수가_아닌_수_손님_수_변경_예외() {
        // given
        OrderTable orderTable = OrderTable.of(1, false);
        Order order = Order.of(orderTable);

        // when
        Throwable thrown = catchThrowable(() -> orderTable.changeNumberOfGuests(-1));

        // then
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("손님 수 변경 요청은 0명 이상 가능합니다.");
    }
}
