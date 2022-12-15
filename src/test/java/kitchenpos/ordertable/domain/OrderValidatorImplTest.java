package kitchenpos.ordertable.domain;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.exception.ExceptionMessage;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.exception.CannotUnGroupOrderTablesException;
import kitchenpos.order.exception.EmptyOrderTableException;
import kitchenpos.ordertable.exception.CannotChangeEmptyException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DisplayName("주문 유효성에 대한 테스트")
@DataJpaTest
class OrderValidatorImplTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderValidatorImpl orderValidator;

    @DisplayName("주문 생성 시 주문테이블이 존재하지 않으면 예외가 발생한다.")
    @Test
    void checkOrderTableExist() {
        when(orderTableRepository.findById(any())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> orderValidator.validate(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageStartingWith(ExceptionMessage.ORDER_TABLE_NOT_FOUND);
    }

    @DisplayName("주문 생성 시 주문테이블이 empty이면 예외가 발생한다.")
    @Test
    void checkOrderTableIsNotEmpty() {
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(OrderTable.of(1, true)));

        Assertions.assertThatThrownBy(() -> orderValidator.validate(1L))
                .isInstanceOf(EmptyOrderTableException.class)
                .hasMessageStartingWith(ExceptionMessage.EMPTY_ORDER_TABLE);
    }

    @DisplayName("주문테이블의 empty 변경 시 주문 하나라도 상태가 조리이면 예외가 발생한다.")
    @Test
    void checkEmptyChangeable() {
        List<OrderLineItem> 주문항목 = Arrays.asList(OrderLineItem.of(1L, 1));
        when(orderRepository.findAllByOrderTableId(any())).thenReturn(
                Arrays.asList(Order.of(1L, 주문항목)));

        Assertions.assertThatThrownBy(() -> orderValidator.checkEmptyChangeable(1L))
                .isInstanceOf(CannotChangeEmptyException.class)
                .hasMessageStartingWith(ExceptionMessage.CAN_NOT_CHANGE_EMPTY_WHEN_COOKING_OR_MEAL);
    }

    @DisplayName("주문테이블의 empty 변경 시 주문 하나라도 상태가 식사이면 예외가 발생한다.")
    @Test
    void checkEmptyChangeable2() {
        List<OrderLineItem> 주문항목 = Arrays.asList(OrderLineItem.of(1L, 1));
        Order 주문 = Order.of(1L, 주문항목);
        주문.changeOrderStatus(OrderStatus.MEAL);
        when(orderRepository.findAllByOrderTableId(any())).thenReturn(Arrays.asList(주문));

        Assertions.assertThatThrownBy(() -> orderValidator.checkEmptyChangeable(1L))
                .isInstanceOf(CannotChangeEmptyException.class)
                .hasMessageStartingWith(ExceptionMessage.CAN_NOT_CHANGE_EMPTY_WHEN_COOKING_OR_MEAL);
    }

    @DisplayName("주문테이블의 단체지정 취소 시 주문 하나라도 상태가 조리이면 예외가 발생한다.")
    @Test
    void checkUnGroupable() {
        List<OrderLineItem> 주문항목1 = Arrays.asList(OrderLineItem.of(1L, 1));
        List<OrderLineItem> 주문항목2 = Arrays.asList(OrderLineItem.of(2L, 1));
        when(orderRepository.findAllByOrderTableIdIn(anyList())).thenReturn(
                Arrays.asList(Order.of(1L, 주문항목1), Order.of(2L, 주문항목2)));

        Assertions.assertThatThrownBy(() -> orderValidator.checkUnGroupable(Arrays.asList(1L, 2L)))
                .isInstanceOf(CannotUnGroupOrderTablesException.class)
                .hasMessageStartingWith(ExceptionMessage.CAN_NOT_UN_GROUP_ORDER_TABLES);
    }

    @DisplayName("주문테이블의 단체지정 취소 시 주문 하나라도 상태가 식사이면 예외가 발생한다.")
    @Test
    void checkUnGroupable2() {
        List<OrderLineItem> 주문항목1 = Arrays.asList(OrderLineItem.of(1L, 1));
        List<OrderLineItem> 주문항목2 = Arrays.asList(OrderLineItem.of(2L, 1));
        Order 주문 = Order.of(1L, 주문항목1);
        주문.changeOrderStatus(OrderStatus.MEAL);
        when(orderRepository.findAllByOrderTableIdIn(anyList())).thenReturn(
                Arrays.asList(주문, Order.of(2L, 주문항목2)));

        Assertions.assertThatThrownBy(() -> orderValidator.checkUnGroupable(Arrays.asList(1L, 2L)))
                .isInstanceOf(CannotUnGroupOrderTablesException.class)
                .hasMessageStartingWith(ExceptionMessage.CAN_NOT_UN_GROUP_ORDER_TABLES);
    }
}
