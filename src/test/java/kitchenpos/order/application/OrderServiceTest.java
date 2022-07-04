package kitchenpos.order.application;

import kitchenpos.common.domain.OrderStatus;
import kitchenpos.common.exception.InvalidOrderStatusException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.dto.OrderLineItemRequestDto;
import kitchenpos.order.dto.OrderLineItemResponseDto;
import kitchenpos.order.dto.OrderRequestDto;
import kitchenpos.order.dto.OrderResponseDto;
import kitchenpos.order.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static kitchenpos.common.fixture.OrderFixture.주문_데이터_생성;
import static kitchenpos.common.fixture.OrderFixture.주문_요청_데이터_생성;
import static kitchenpos.common.fixture.OrderLineItemFixture.주문항목_데이터_생성;
import static kitchenpos.common.fixture.OrderLineItemFixture.주문항목_요청_데이터_생성;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderValidator orderValidator;


    @BeforeEach
    void setUp() {
        orderService = new OrderService(orderRepository, orderValidator);
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void create() {
        //given
        List<OrderLineItemRequestDto> orderLineItemRequests = Arrays.asList(주문항목_요청_데이터_생성(1L, 1));
        OrderRequestDto request = 주문_요청_데이터_생성(orderLineItemRequests);

        List<OrderLineItem> orderLineItems = Arrays.asList(주문항목_데이터_생성(1L, 1L, 2));
        given(orderRepository.save(any())).willReturn(주문_데이터_생성(1L, 1L, OrderStatus.COOKING, orderLineItems));

        //when
        OrderResponseDto response = orderService.create(request);

        //then
        주문_데이터_확인(response, 1L, 1L, OrderStatus.COOKING);
        주문항목_데이터_확인(response.getOrderLineItems().get(0), 1L, 1L, 1L, 2);
    }

    @DisplayName("주문항목이 하나도 없으면 생성할 수 없다.")
    @Test
    void create_fail_menuNotExists() {
        //given
        OrderRequestDto request = 주문_요청_데이터_생성(Collections.emptyList());

        //when //then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(request));
    }

    @DisplayName("주문과 주문항목을 전체 조회한다.")
    @Test
    void list() {
        //given
        Order order = 주문_데이터_통합_생성(OrderStatus.COOKING);

        given(orderRepository.findAll()).willReturn(Arrays.asList(order));

        //when
        List<OrderResponseDto> list = orderService.list();

        //then
        assertEquals(1, list.size());
    }

    @DisplayName("주문상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        //given
        given(orderRepository.findById(any())).willReturn(Optional.of(주문_데이터_통합_생성(OrderStatus.COOKING)));

        //when
        OrderResponseDto response = orderService.changeOrderStatus(1L, OrderStatus.MEAL);

        //then
        주문_데이터_확인(response, 1L, 1L, OrderStatus.MEAL);
    }

    @DisplayName("해당 주문 id가 없는경우, 상태를 변경할 수 없다.")
    @Test
    void changeOrderStatus_fail_notExistsOrder() {
        //given
        given(orderRepository.findById(any())).willReturn(Optional.empty());

        //when //then
        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> orderService.changeOrderStatus(1L, OrderStatus.MEAL));
    }

    @DisplayName("주문상태가 계산완료인 경우, 상태를 변경할 수 없다")
    @Test
    void changeOrderStatus_fail_statusComplete() {
        //given
        given(orderRepository.findById(any())).willReturn(Optional.of(주문_데이터_통합_생성(OrderStatus.COMPLETION)));

        //when //then
        assertThatExceptionOfType(InvalidOrderStatusException.class)
                .isThrownBy(() -> orderService.changeOrderStatus(1L, OrderStatus.MEAL));
    }

    private Order 주문_데이터_통합_생성(OrderStatus status) {
        List<OrderLineItem> orderLineItems = Arrays.asList(주문항목_데이터_생성(1L, 1L, 2));
        Order order = 주문_데이터_생성(1L, 1L, status, orderLineItems);
        return order;
    }

    private void 주문_데이터_확인(OrderResponseDto order, Long id, Long tableId, OrderStatus orderStatus) {
        assertAll(
                () -> assertEquals(id, order.getId()),
                () -> assertEquals(tableId, order.getOrderTableId()),
                () -> assertEquals(orderStatus, order.getOrderStatus()),
                () -> assertThat(order.getOrderLineItems()).isNotEmpty()
        );
    }

    private void 주문항목_데이터_확인(OrderLineItemResponseDto orderLineItem, Long seq, Long orderId, Long menuId, int quantity) {
        assertAll(
                () -> assertEquals(seq, orderLineItem.getSeq()),
                () -> assertEquals(menuId, orderLineItem.getMenuId()),
                () -> assertEquals(quantity, orderLineItem.getQuantity())
        );
    }

}