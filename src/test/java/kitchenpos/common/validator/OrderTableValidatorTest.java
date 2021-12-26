package kitchenpos.common.validator;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import kitchenpos.common.exception.OrderIsNotCompleteException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.exception.ClosedTableOrderException;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.testfixtures.TableTestFixtures;
import kitchenpos.ordertable.vo.NumberOfGuests;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderTableValidatorTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderTableValidator orderTableValidator;

    @DisplayName("테이블이 주문종료 상태이면 예외")
    @Test
    void validateNotOrderClosedTable() {
        //given
        OrderTable orderTable = new OrderTable(1L, new NumberOfGuests(6), true);
        TableTestFixtures.특정_주문테이블_조회_모킹(orderTableRepository, orderTable);

        //when, then
        assertThatThrownBy(
            () -> orderTableValidator.validateNotOrderClosedTable(orderTable.getId()))
            .isInstanceOf(ClosedTableOrderException.class);
    }

    @DisplayName("주문상태가 계산완료가 아닌 주문이 있는 경우 예외")
    @Test
    void validateAllOrdersInTableComplete() {
        //given
        List<Order> orders = Arrays.asList(
            new Order(1L, 1L, OrderStatus.COMPLETION, Lists.emptyList()),
            new Order(2L, 1L, OrderStatus.COMPLETION, Lists.emptyList()),
            new Order(3L, 1L, OrderStatus.COOKING, Lists.emptyList()));
        특정_테이블의_전체주문_조회_모킹(orders);

        //when, then
        assertThatThrownBy(
            () -> orderTableValidator.validateAllOrdersInTableComplete(1L))
            .isInstanceOf(OrderIsNotCompleteException.class);
    }

    void 특정_테이블의_전체주문_조회_모킹(List<Order> expectedOrders) {
        given(orderRepository.findAllByOrderTableId(any()))
            .willReturn(expectedOrders);
    }
}
