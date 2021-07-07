package kitchenpos.order.application;

import static kitchenpos.table.application.TableServiceTest.두명;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 서비스")
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    private static final boolean 비어있음 = true;
    private static final boolean 비어있지않음 = false;
    private static final Long 주문테이블_ID = 1L;
    private static final Long 주문_ID = 1L;
    private static final OrderTable 주문테이블 = new OrderTable(주문테이블_ID, 두명, 비어있지않음);
    private static final OrderLineItem 첫번째_주문항목 = new OrderLineItem(주문_ID, 1L, 1);
    private static final OrderLineItem 두번째_주문항목 = new OrderLineItem(주문_ID, 2L, 1);
    private static final List<OrderLineItem> 주문_항목_목록 = new ArrayList<>(Arrays.asList(첫번째_주문항목, 두번째_주문항목));

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @InjectMocks
    private OrderService orderService;

    @DisplayName("주문을 등록한다.")
    @Test
    void create() {
        // Given
        OrderRequest 주문 = new OrderRequest(주문테이블_ID, OrderStatus.COOKING, LocalDateTime.now(), OrderLineItemRequest.listOf(주문_항목_목록));
        given(orderTableRepository.findById(any())).willReturn(Optional.of(주문테이블));
        given(orderRepository.save(any())).willReturn(주문.toOrder());

        // When
        orderService.create(주문);

        // Then
        verify(orderTableRepository, times(1)).findById(any());
        verify(orderRepository, times(1)).save(any());
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void list() {
        // Given
        OrderRequest 주문 = new OrderRequest(주문테이블_ID, OrderStatus.COOKING, LocalDateTime.now(), OrderLineItemRequest.listOf(주문_항목_목록));
        List<Order> 주문_목록 = new ArrayList<>(Arrays.asList(주문.toOrder()));
        given(orderRepository.findAll()).willReturn(주문_목록);

        // When & Then
        assertThat(orderService.list()).hasSize(1);
        verify(orderRepository, times(1)).findAll();
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        // Given
        OrderStatus 진행상태 = OrderStatus.COOKING;
        OrderRequest 주문 = new OrderRequest(주문_ID, 주문테이블_ID, 진행상태, LocalDateTime.now(), OrderLineItemRequest.listOf(주문_항목_목록));
        given(orderRepository.findById(any())).willReturn(Optional.of(주문.toOrder()));

        // When
        orderService.changeOrderStatus(주문_ID, 진행상태);

        // Then
        verify(orderRepository, times(1)).findById(any());
    }

}
