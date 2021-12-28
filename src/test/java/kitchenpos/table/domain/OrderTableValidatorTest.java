package kitchenpos.table.domain;

import kitchenpos.common.domain.Quantity;
import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.IllegalArgumentException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;

@DisplayName("주문 테이블 검증 테스트")
@ExtendWith(MockitoExtension.class)
public class OrderTableValidatorTest {
    @InjectMocks
    private OrderTableValidator orderTableValidator;

    @Mock
    private OrderRepository orderRepository;

    @DisplayName("완료되지 않은 주문이 있을 경우 빈 테이블 여부를 변경할 수 없다.")
    @Test
    void 미완료_주문_존재_빈_테이블_여부_변경_예외() {
        // given
        OrderTable orderTable = OrderTable.of(5, false);
        given(orderRepository.findOrderByOrderTableId(null)).willReturn(Collections.singletonList(Order.of(1L, new ArrayList<>())));

        // when
        Throwable thrown = catchThrowable(() -> orderTable.changeEmpty(orderTableValidator, true));

        // then
        assertThat(thrown).isInstanceOf(BadRequestException.class)
                .hasMessage("현재 테이블은 주문 완료 상태가 아니므로 빈 테이블 설정을 할 수 없습니다.");
    }

    @DisplayName("테이블 그룹이 존재할 경우 빈 테이블 여부를 변경할 수 없다.")
    @Test
    void 테이블_그룹_존재_주문_존재_빈_테이블_여부_변경_예외() {
        // given
        OrderTable orderTable = OrderTable.of(5, false);
        orderTable.addGroup(1L);

        // when
        Throwable thrown = catchThrowable(() -> orderTable.changeEmpty(orderTableValidator, true));

        // then
        assertThat(thrown).isInstanceOf(BadRequestException.class)
                .hasMessage("테이블 그룹이 존재하므로 빈 테이블 설정을 할 수 없습니다.");
    }

    @DisplayName("손님 수를 변경한다.")
    @Test
    void 손님_수_변경() {
        // given
        OrderTable orderTable = OrderTable.of(5, false);
        Order order = Order.of(1L, Collections.singletonList(OrderLineItem.of(1L, Quantity.of(2L))));

        // when
        orderTable.changeNumberOfGuests(orderTableValidator, 4);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(4);
    }

    @DisplayName("양수가 아닌 수로 손님 수를 변경할 수 없다.")
    @Test
    void 양수가_아닌_수_손님_수_변경_예외() {
        // given
        OrderTable orderTable = OrderTable.of(1, false);
        Order order = Order.of(1L, Collections.singletonList(OrderLineItem.of(1L, Quantity.of(2L))));

        // when
        Throwable thrown = catchThrowable(() -> orderTable.changeNumberOfGuests(orderTableValidator, -1));

        // then
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("손님 수는 최소 0명 이상 설정 가능합니다.");
    }

    @DisplayName("빈 테이블의 손님 수를 변경할 수 없다.")
    @Test
    void 빈_테이블_손님_수_변경_예외() {
        // given
        OrderTable orderTable = OrderTable.of(0, true);

        // when
        Throwable thrown = catchThrowable(() -> orderTable.changeNumberOfGuests(orderTableValidator, 1));

        // then
        assertThat(thrown).isInstanceOf(BadRequestException.class)
                .hasMessage("빈 테이블의 손님 수를 설정할 수 없습니다.");
    }
}
