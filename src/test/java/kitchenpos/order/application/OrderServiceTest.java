package kitchenpos.order.application;

import kitchenpos.common.exception.*;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static kitchenpos.menu.domain.MenuTest.치킨세트;
import static kitchenpos.table.domain.OrderTableTest.빈자리;
import static kitchenpos.table.domain.OrderTableTest.이인석;
import static kitchenpos.order.domain.OrderTest.주문통합;
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
    private OrderTableRepository orderTableRepository;
    @InjectMocks
    private OrderService orderService;

    @Mock
    private Order order;

    private final OrderRequest orderRequest = new OrderRequest(
            1L
            , Collections.singletonList(new OrderLineItemRequest(1L, 2L))
    );

    private final OrderStatusRequest orderStatusRequest = new OrderStatusRequest(OrderStatus.COOKING.name());

    @Test
    @DisplayName("주문 등록")
    void orderCreateTest() {
        // given
        given(orderTableRepository.findByIdElseThrow(anyLong())).willReturn(이인석);
        given(menuRepository.findByIdElseThrow(anyLong())).willReturn(치킨세트);

        given(orderRepository.save(any())).willReturn(주문통합);
        // when
        OrderResponse actual = orderService.create(orderRequest);
        // then
        verify(orderRepository, only()).save(any());
        assertThat(actual).isEqualTo(OrderResponse.of(주문통합));
    }

    @Test
    @DisplayName("주문 항목이 없으면 주문을 등록 할 수 없다.")
    void notFoundOrderLineItem() {
        // given
        OrderRequest 요청_데이터 = mock(OrderRequest.class);
        given(orderTableRepository.findByIdElseThrow(anyLong())).willReturn(이인석);
        // when
        // then
        assertThatThrownBy(() -> orderService.create(요청_데이터))
                .isInstanceOf(OrderLineItemEmptyException.class);
    }

    @Test
    @DisplayName("등록이 안된 메뉴는 주문 할 수 없다.")
    void notFoundMenu() {
        // given
        given(orderTableRepository.findByIdElseThrow(anyLong())).willReturn(이인석);
        given(menuRepository.findByIdElseThrow(anyLong())).willThrow(NotFoundMenuException.class);
        // when
        // then
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(NotFoundMenuException.class);
    }

    @Test
    @DisplayName("등록이 안된 주문 테이블에서는 주문을 할 수 없다.")
    void notFoundOrderTable() {
        // given
        given(orderTableRepository.findByIdElseThrow(anyLong())).willThrow(NotFoundOrderTableException.class);
        // when
        // then
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(NotFoundOrderTableException.class);
        verify(orderTableRepository, only()).findByIdElseThrow(anyLong());
    }

    @Test
    @DisplayName("빈 테이블에서는 주문을 등록 할 수 없다.")
    void notOrderEmptyTable() {
        // given
        given(orderTableRepository.findByIdElseThrow(anyLong())).willReturn(빈자리);
        // when
        // then
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(NotOrderedEmptyTableException.class);
        verify(orderTableRepository, only()).findByIdElseThrow(anyLong());
    }

    @Test
    @DisplayName("주문 리스트 조회")
    void listTest() {
        // given
        given(orderRepository.findAll()).willReturn(Collections.singletonList(order));
        // when
        List<OrderResponse> actual = orderService.list();
        // then
        assertThat(actual).hasSize(1);
        verify(orderRepository, only()).findAll();
    }

    @Test
    @DisplayName("주문 상태 변경")
    void changeOrderStatus() {
        // given
        given(orderRepository.findByIdElseThrow(anyLong())).willReturn(주문통합);
        // when
        OrderResponse actual = orderService.changeOrderStatus(1L, orderStatusRequest);
        // then
        assertThat(actual).isEqualTo(OrderResponse.of(주문통합));
        verify(orderRepository, only()).findByIdElseThrow(anyLong());
    }

    @Test
    @DisplayName("등록되지 않은 주문의 상태는 변경할 수 없다.")
    void notFoundOrder() {
        // given
        given(orderRepository.findByIdElseThrow(anyLong())).willThrow(NotFoundOrderException.class);
        // when
        // then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, any()))
                .isInstanceOf(NotFoundOrderException.class);
    }

    @Test
    @DisplayName("`계산완료(COMPLETION)`상태인 주문은 변경할 수 없다.")
    void notChangeCompletionStatus() {
        // given
        given(orderRepository.findByIdElseThrow(anyLong())).willReturn(order);
        doThrow(new NotChangeCompletionOrderException()).when(order).changeOrderStatus(any());
        // when
        // then
        assertThatThrownBy(() -> orderService.changeOrderStatus(
                1L, orderStatusRequest)
        ).isInstanceOf(NotChangeCompletionOrderException.class);
        verify(orderRepository, only()).findByIdElseThrow(anyLong());
    }
}
