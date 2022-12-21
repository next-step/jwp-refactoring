package kitchenpos.order.domain;

import kitchenpos.exception.BadRequestException;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.utils.Message.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@DisplayName("주문 유효성에 대한 테스트")
@DataJpaTest
class OrderValidatorImplTest {

    private OrderMenu menu1 = OrderMenu.of(1L, "honeycombo", BigDecimal.valueOf(18000));
    private OrderMenu menu2 = OrderMenu.of(2L, "redwing", BigDecimal.valueOf(20000));

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
                .hasMessageStartingWith(EMPTY_ORDER_TABLE);
    }

    @DisplayName("주문 생성 시 주문테이블이 empty이면 예외가 발생한다.")
    @Test
    void checkOrderTableIsNotEmpty() {
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(OrderTable.of(null, 1, true)));

        Assertions.assertThatThrownBy(() -> orderValidator.validate(1L))
                .isInstanceOf(BadRequestException.class)
                .hasMessageStartingWith(NOT_EXISTS_ORDER_TABLE);
    }

    @DisplayName("주문테이블의 empty 변경 시 주문 하나라도 상태가 조리이면 예외가 발생한다.")
    @Test
    void checkEmptyChangeable() {
        List<OrderLineItem> orderItems = Arrays.asList(OrderLineItem.of(menu1, 1));
        when(orderRepository.findAllByOrderTableId(any())).thenReturn(
                Arrays.asList(Order.of(1L, orderItems)));

        Assertions.assertThatThrownBy(() -> orderValidator.checkEmptyChangeable(1L))
                .isInstanceOf(BadRequestException.class)
                .hasMessageStartingWith(INVALID_CHANGE_TO_EMPTY_ORDER_STATUS);
    }

    @DisplayName("주문테이블의 empty 변경 시 주문 하나라도 상태가 식사이면 예외가 발생한다.")
    @Test
    void checkEmptyChangeable2() {
        List<OrderLineItem> orderItems = Arrays.asList(OrderLineItem.of(menu1, 1));
        Order order = Order.of(1L, orderItems);
        order.changeOrderStatus(OrderStatus.MEAL);
        when(orderRepository.findAllByOrderTableId(any())).thenReturn(Arrays.asList(order));

        Assertions.assertThatThrownBy(() -> orderValidator.checkEmptyChangeable(1L))
                .isInstanceOf(BadRequestException.class)
                .hasMessageStartingWith(INVALID_CHANGE_TO_EMPTY_ORDER_STATUS);
    }

    @DisplayName("주문테이블의 단체지정 취소 시 주문 하나라도 상태가 조리이면 예외가 발생한다.")
    @Test
    void checkUnGroupable() {
        List<OrderLineItem> orderItem1 = Arrays.asList(OrderLineItem.of(menu1, 1));
        List<OrderLineItem> orderItem2 = Arrays.asList(OrderLineItem.of(menu2, 1));
        when(orderRepository.findAllByOrderTableIdIn(anyList())).thenReturn(
                Arrays.asList(Order.of(1L, orderItem1), Order.of(2L, orderItem2)));

        Assertions.assertThatThrownBy(() -> orderValidator.checkCanBeUngrouped(Arrays.asList(1L, 2L)))
                .isInstanceOf(BadRequestException.class)
                .hasMessageStartingWith(INVALID_CANCEL_ORDER_TABLES_STATUS);
    }

    @DisplayName("주문테이블의 단체지정 취소 시 주문 하나라도 상태가 식사이면 예외가 발생한다.")
    @Test
    void checkUnGroupable2() {
        List<OrderLineItem> orderItem1 = Arrays.asList(OrderLineItem.of(menu1, 1));
        List<OrderLineItem> orderItem2 = Arrays.asList(OrderLineItem.of(menu2, 1));
        Order order = Order.of(1L, orderItem1);
        order.changeOrderStatus(OrderStatus.MEAL);
        when(orderRepository.findAllByOrderTableIdIn(anyList())).thenReturn(
                Arrays.asList(order, Order.of(2L, orderItem2)));

        Assertions.assertThatThrownBy(() -> orderValidator.checkCanBeUngrouped(Arrays.asList(1L, 2L)))
                .isInstanceOf(BadRequestException.class)
                .hasMessageStartingWith(INVALID_CANCEL_ORDER_TABLES_STATUS);
    }
}
