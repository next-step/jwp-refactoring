package kitchenpos.application;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static kitchenpos.domain.OrderLineItemTest.와퍼_세트_주문;
import static kitchenpos.domain.OrderLineItemTest.콜라_주문;
import static kitchenpos.domain.OrderTableTest.빈자리;
import static kitchenpos.domain.OrderTableTest.이인석;
import static kitchenpos.domain.OrderTest.주문통합;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("주문 관리 테스트")
public class OrderServiceTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderLineItemRepository orderLineItemRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @InjectMocks
    private OrderService orderService;

    @Mock
    private Order order;

    @Test
    @DisplayName("주문 등록")
    void orderCreateTest() {
        // given
        given(order.getOrderLineItems()).willReturn(Arrays.asList(와퍼_세트_주문, 콜라_주문));
        given(menuRepository.countByIdIn(anyList())).willReturn(2L);
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(이인석));
        given(orderRepository.save(any())).willReturn(order);
        given(orderLineItemRepository.save(any())).willReturn(와퍼_세트_주문, 콜라_주문);
        // when
        Order actual = orderService.create(order);
        // then
        assertThat(actual).isEqualTo(order);
        verify(orderLineItemRepository, times(2)).save(any());
    }

    @Test
    @DisplayName("주문 항목이 없으면 주문을 등록 할 수 없다.")
    void notFoundOrderLineItem() {
        // given
        given(order.getOrderLineItems()).willReturn(Collections.emptyList());
        // when
        // then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("등록이 안된 메뉴는 주문 할 수 없다.")
    void notFoundMenu() {
        // given
        given(order.getOrderLineItems()).willReturn(Arrays.asList(와퍼_세트_주문, 콜라_주문));
        given(menuRepository.countByIdIn(anyList())).willReturn(0L);
        // when
        // then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("등록이 안된 주문 테이블에서는 주문을 할 수 없다.")
    void notFoundOrderTable() {
        // given
        given(order.getOrderTableId()).willReturn(1L);
        given(order.getOrderLineItems()).willReturn(Arrays.asList(와퍼_세트_주문, 콜라_주문));
        given(menuRepository.countByIdIn(anyList())).willReturn(2L);
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
        verify(orderTableRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("빈 테이블에서는 주문을 등록 할 수 없다.")
    void notOrderEmptyTable() {
        // given
        given(menuRepository.countByIdIn(anyList())).willReturn(2L);
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(빈자리));
        // when
        // then
        assertThatThrownBy(() -> orderService.create(주문통합))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 리스트 조회")
    void listTest() {
        // given
        given(order.getId()).willReturn(1L);
        given(orderRepository.findAll()).willReturn(Collections.singletonList(order));
        given(orderLineItemRepository.findAllByOrderId(anyLong())).willReturn(Arrays.asList(와퍼_세트_주문, 콜라_주문));
        // when
        List<Order> actual = orderService.list();
        // then
        assertThat(actual).hasSize(1);
        verify(orderLineItemRepository, times(1)).findAllByOrderId(anyLong());
    }

    @Test
    @DisplayName("주문 상태 변경")
    void changeOrderStatus() {
        // given
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(주문통합));
        given(orderRepository.save(any())).willReturn(주문통합);
        // when
        Order actual = orderService.changeOrderStatus(1L, 주문통합);
        // then
        assertThat(actual).isEqualTo(주문통합);
    }

    @Test
    @DisplayName("등록되지 않은 주문의 상태는 변경할 수 없다.")
    void notFoundOrder() {
        // given
        given(orderRepository.findById(anyLong())).willReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, 주문통합))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("`계산완료(COMPLETION)`상태인 주문은 변경할 수 없다.")
    void notChangeCompletionStatus() {
        // given
        given(order.getOrderStatus()).willReturn(OrderStatus.COMPLETION);
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));
        // when
        // then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, order))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
