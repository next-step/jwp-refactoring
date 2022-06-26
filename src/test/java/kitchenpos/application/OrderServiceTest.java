package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.core.exception.BadRequestException;
import kitchenpos.core.exception.CannotCreateException;
import kitchenpos.core.exception.ExceptionType;
import kitchenpos.core.exception.NotFoundException;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.request.OrderRequest;
import kitchenpos.order.domain.response.OrderResponse;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.application.OrderService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.order.domain.request.OrderLineItemRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("상품 서비스에 대한 테스트")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private OrderService orderService;

    private OrderTable 주문_테이블_entity;

    private OrderLineItemRequest 주문_항목_request;
    private OrderLineItemRequest 주문_항목_request2;
    private OrderRequest 주문_request;

    private OrderLineItem 주문_항목;
    private OrderLineItem 주문_항목2;
    private Order 주문;

    @BeforeEach
    void setUp() {
        주문_테이블_entity = OrderTable.of(1L, null, 3, false);

        주문_항목_request = new OrderLineItemRequest(1L, 1);
        주문_항목_request2 = new OrderLineItemRequest(2L, 1);
        주문_request = new OrderRequest(1L, null, null, Arrays.asList(주문_항목_request, 주문_항목_request2));

        주문_항목 = OrderLineItem.of(1L, 주문, 1L, 1);
        주문_항목2 = OrderLineItem.of(2L, 주문, 1L, 1);
        주문 = Order.of(주문_테이블_entity, Arrays.asList(주문_항목, 주문_항목2));
    }

    @DisplayName("주문을 등록하면 정상적으로 등록되어야 한다")
    @Test
    void create_test() {
        // given
        when(menuRepository.countByIdIn(
            Arrays.asList(주문_항목_request.getMenuId(), 주문_항목_request2.getMenuId())))
            .thenReturn(2L);
        when(orderTableRepository.findById(주문_request.getOrderTableId()))
            .thenReturn(Optional.of(주문_테이블_entity));
        when(orderRepository.save(any()))
            .thenReturn(주문);

        // when
        OrderResponse result = orderService.create(주문_request);

        // then
        assertAll(
            () -> assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COOKING),
            () -> assertNotNull(result.getOrderedTime()),
            () -> assertThat(result.getOrderLineItems()).hasSize(2)
        );
    }

    @DisplayName("주문등록시 주문의 항목이 비어있다면 예외가 발생한다")
    @Test
    void create_exception_test() {
        // given
        주문_request = new OrderRequest(1L, null, null, Collections.emptyList());

        // then
        assertThatThrownBy(() -> {
            orderService.create(주문_request);
        }).isInstanceOf(BadRequestException.class)
            .hasMessageContaining(ExceptionType.EMPTY_ORDER_LINE_ITEM.getMessage());
    }

    @DisplayName("주문등록시 주문 항목의 메뉴중 존재하지 않는 메뉴가 있다면 예외가 발생한다")
    @Test
    void create_exception_test2() {
        // given
        when(menuRepository.countByIdIn(Arrays.asList(
            주문_항목_request.getMenuId(), 주문_항목_request2.getMenuId())))
            .thenReturn(1L);

        // then
        assertThatThrownBy(() -> {
            orderService.create(주문_request);
        }).isInstanceOf(CannotCreateException.class)
            .hasMessageContaining(ExceptionType.CONTAINS_NOT_EXIST_MENU.getMessage());
    }

    @DisplayName("주문등록시 주문 테이블이 존재하지 테이블이라면 예외가 발생한다")
    @Test
    void create_exception_test3() {
        // given
        when(menuRepository.countByIdIn(Arrays.asList(
            주문_항목_request.getMenuId(), 주문_항목_request2.getMenuId())))
            .thenReturn(2L);
        when(orderTableRepository.findById(주문_request.getOrderTableId()))
            .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> {
            orderService.create(주문_request);
        }).isInstanceOf(NotFoundException.class)
            .hasMessageContaining(ExceptionType.NOT_EXIST_ORDER_TABLE.getMessage());
    }

    @DisplayName("주문상태를 변경하면 정상적으로 변경되어야 한다")
    @Test
    void change_order_test() {
        // given
        주문_request = new OrderRequest(null, OrderStatus.MEAL, null, null);
        when(orderRepository.findById(주문.getId()))
            .thenReturn(Optional.of(주문));
        when(orderRepository.findAllOrderAndItemsByOrder(주문))
            .thenReturn(주문);

        // when
        OrderResponse result = orderService.changeOrderStatus(주문.getId(), 주문_request);

        // then
        assertThat(result.getId()).isEqualTo(주문.getId());
        assertThat(result.getOrderStatus()).isEqualTo(주문_request.getOrderStatus());
    }

    @DisplayName("주문상태를 변경할 주문이 없다면 예외가 발생한다")
    @Test
    void change_order_exception_test() {
        // given
        when(orderRepository.findById(주문.getId()))
            .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> {
            orderService.changeOrderStatus(주문.getId(), 주문_request);
        }).isInstanceOf(NotFoundException.class)
            .hasMessageContaining(ExceptionType.NOT_EXIST_ORDER.getMessage());
    }

    @DisplayName("주문목록을 조회한다")
    @Test
    void findAll_test() {
        // given
        when(orderRepository.findAllOrderAndItems())
            .thenReturn(Collections.singletonList(주문));

        // when
        List<OrderResponse> result = orderService.list();

        // then
        assertThat(result).hasSize(1);
    }
}
