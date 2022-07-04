package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {

    @Mock
    OrderTableRepository orderTableRepository;

    @Mock
    MenuRepository menuRepository;

    OrderValidator orderValidator;

    @DisplayName("주문 테이블 없이 주문을 할 수 없다.")
    @Test
    void createOrder_without_order_table() {
        // given
        Order order = new Order(1L, null, OrderStatus.COOKING, Collections.singletonList(new OrderLineItem(1L, 1)));
        when(orderTableRepository.findById(any())).thenReturn(Optional.empty());
        orderValidator = new OrderValidator(orderTableRepository, menuRepository);

        // when, then
        assertThatThrownBy(() -> {
            orderValidator.validate(order);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈(emtpy) 테이블에서는 주문을 할 수 없다.")
    @Test
    void createOrder_with_empty_order_table() {
        // given
        Order order = new Order(1L, null, OrderStatus.COOKING, Collections.singletonList(new OrderLineItem(1L, 1)));
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(new OrderTable(1L, 3, true)));
        orderValidator = new OrderValidator(orderTableRepository, menuRepository);

        // when, then
        assertThatThrownBy(() -> {
            orderValidator.validate(order);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴에 없는 주문 항목으로 주문이 불가하다.")
    @Test
    void validateCheck1() {
        // given
        Order order = new Order(1L, null, OrderStatus.COOKING, Collections.singletonList(new OrderLineItem(1L, 1)));
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(new OrderTable(1L)));
        when(menuRepository.findAllById(any())).thenReturn(Collections.emptyList());
        orderValidator = new OrderValidator(orderTableRepository, menuRepository);

        // when, then
        assertThatThrownBy(() -> {
            orderValidator.validate(order);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 수와 주문 항목의 수가 일치하지 않으면 주문이 불가하다.")
    @Test
    void validateCheck2() {
        Menu menu = new Menu(1L);
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, null, menu.getId(), 1);
        OrderLineItem orderLineItem2 = new OrderLineItem(1L, null, menu.getId(), 1);
        Order order = new Order(1L, null, OrderStatus.COOKING, Arrays.asList(orderLineItem1, orderLineItem2));

        // given
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(new OrderTable(1L)));
        when(menuRepository.findAllById(any())).thenReturn(Collections.singletonList(menu));
        orderValidator = new OrderValidator(orderTableRepository, menuRepository);

        // when, then
        assertThatThrownBy(() -> {
            orderValidator.validate(order);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
