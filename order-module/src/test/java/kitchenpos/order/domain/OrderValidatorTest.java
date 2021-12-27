package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;

@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @InjectMocks
    private OrderValidator orderValidator;

    @DisplayName("주문 항목이 비어있을 때 예외 발생")
    @Test
    void validateNotEmptyOrderLineItems() {
        // given
        final Order order = new Order(1L, OrderStatus.MEAL, Collections.emptyList());

        // when, then
        assertThatIllegalArgumentException()
            .isThrownBy(() -> orderValidator.validateToCreate(order))
            .withMessageContaining("주문 항목이 비어있습니다");
    }

    @DisplayName("주문 항목의 메뉴의 갯수가 실제로 존재하는 갯수와 일치하지 않을 때 예외 발생")
    @Test
    void validateCorrectMenuId() {
        // given
        final List<OrderLineItem> orderLineItems = Arrays.asList(new OrderLineItem(1L, 1L), new OrderLineItem(2L, 1L));
        final Order order = new Order(1L, OrderStatus.MEAL, orderLineItems);
        when(menuRepository.countByIdIn(anyCollection())).thenReturn(1);

        // when, then
        assertThatIllegalArgumentException()
            .isThrownBy(() -> orderValidator.validateToCreate(order))
            .withMessageContaining("메뉴 아이디의 값이 부정확합니다");
    }

    @DisplayName("빈 테이블에서 주문하려고 할 때 예외 발생")
    @Test
    void validateNotEmptyTable() {
        // given
        final List<OrderLineItem> orderLineItems = Arrays.asList(new OrderLineItem(1L, 1L), new OrderLineItem(2L, 1L));
        final Order order = new Order(1L, OrderStatus.MEAL, orderLineItems);
        when(menuRepository.countByIdIn(anyCollection())).thenReturn(2);
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(new OrderTable(1, true)));

        // when, then
        assertThatIllegalArgumentException()
            .isThrownBy(() -> orderValidator.validateToCreate(order))
            .withMessageContaining("빈 테이블에서는 주문을 할 수 없습니다");
    }
}
