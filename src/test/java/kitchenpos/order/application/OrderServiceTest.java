package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menu.application.MenuValidator;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.exception.MenuNotFoundException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.exception.OrderNotFoundException;
import kitchenpos.table.application.OrderTableValidator;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.exception.NonEmptyOrderTableNotFoundException;

@DisplayName("주문 서비스")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private MenuValidator menuValidator;
    @Mock
    private OrderTableValidator orderTableValidator;

    @InjectMocks
    private OrderService orderService;

    private Menu menu;

    @BeforeEach
    void setUp() {
        menu = new Menu("A", BigDecimal.valueOf(20000.00), 1L);
        menu.addMenuProduct(new MenuProduct(menu, 1L, 1));
        menu.addMenuProduct(new MenuProduct(menu, 2L, 1));
    }

    @TestFactory
    @DisplayName("모든 주문을 조회하는 기능")
    List<DynamicTest> find_allOrders1() {
        // mocking
        Order order = new Order(LocalDateTime.now(), 1L);
        order.addOrderLineItem(new OrderLineItem(order, 1L, 3L));
        given(orderRepository.findAll()).willReturn(Arrays.asList(order));

        // when
        List<OrderResponse> findOrderResponses = orderService.findAllOrders();

        // then
        return Arrays.asList(
                dynamicTest("조회 결과 주문 ID 포함 확인됨.", () -> assertThat(findOrderResponses).extracting("orderStatus").contains(order.getOrderStatus())),
                dynamicTest("주문 별 주문 항목 확인 됨.", () -> assertThat(findOrderResponses.get(0).getOrderLineItemResponses()).size().isOne())
        );
    }

    @Test
    @DisplayName("주문 상태 변경 기능")
    void change_orderStatus1() {
        // given
        OrderRequest orderRequest = new OrderRequest(OrderStatus.MEAL, 1L, new ArrayList<>());
        Order order = new Order(LocalDateTime.now(), 1L);
        order.addOrderLineItem(new OrderLineItem(order, 1L, 3L));
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

        // when
        OrderResponse resultOrderResponse = orderService.changeOrderStatus(1L, orderRequest);

        // then
        assertThat(resultOrderResponse.getOrderStatus()).isEqualTo(order.getOrderStatus());
    }

    @TestFactory
    @DisplayName("주문 상태 변경 시 오류 발생 테스트")
    List<DynamicTest> change_exception1() {
        return Arrays.asList(
                dynamicTest("수정 대상 조회 실패 시 오류 발생함.", () -> {
                    // given
                    OrderRequest orderRequest = new OrderRequest(OrderStatus.MEAL, 1L, new ArrayList<>());
                    given(orderRepository.findById(anyLong())).willReturn(Optional.empty());

                    // then
                    assertThatThrownBy(() -> orderService.changeOrderStatus(1L, orderRequest))
                            .isInstanceOf(OrderNotFoundException.class);
                })
        );
    }

    @TestFactory
    @DisplayName("신규 주문 등록 기능")
    List<DynamicTest> create_order1() {
        // given
        OrderRequest orderRequest = new OrderRequest(OrderStatus.COOKING, 1L,
                Arrays.asList(new OrderLineItemRequest(1L, 1L)));
        OrderTable orderTable = new OrderTable(3, false);
        Order order = new Order(LocalDateTime.now(), 1L);
        order.addOrderLineItem(new OrderLineItem(order, 1L, 3L));

        given(orderRepository.save(any(Order.class))).willReturn(order);

        // when
        OrderResponse resultOrderResponse = orderService.create(orderRequest);

        // then
        verify(orderTableValidator).validateExistsOrderTableByIdAndEmptyIsFalse(anyLong());
        verify(menuValidator).validateExistsMenuById(anyLong());
        return Arrays.asList(
                dynamicTest("주문 초기 상태 확인됨.", () -> assertThat(resultOrderResponse.getOrderStatus()).isEqualTo(OrderStatus.COOKING)),
                dynamicTest("주문 테이블 확인됨.", () -> assertThat(resultOrderResponse.getOrderTableId()).isNotNull()),
                dynamicTest("주문 항목 갯수 확인됨.", () -> assertThat(resultOrderResponse.getOrderLineItemResponses()).size().isOne())
        );
    }

    @TestFactory
    @DisplayName("주문 등록 요청 시 예외상황 발생 테스트")
    List<DynamicTest> create_order_exception1() {
        return Arrays.asList(
                dynamicTest("주문 내역 누락 시 오류 발생됨.", () -> {
                    // given
                    OrderRequest orderRequest = new OrderRequest(OrderStatus.COOKING, 1L, new ArrayList<>());

                    // then
                    assertThatThrownBy(() -> orderService.create(orderRequest))
                            .isInstanceOf(IllegalArgumentException.class)
                            .hasMessage("입력된 주문 항목이 없습니다.");
                }),
                dynamicTest("주문 내역 메뉴에 등록되지 않은 메뉴가 포함되어 있을 경우 오류 발생됨.", () -> {
                    // given
                    OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 1L);
                    OrderRequest orderRequest = new OrderRequest(OrderStatus.COOKING, 2L, Arrays.asList(orderLineItemRequest));
                    doThrow(MenuNotFoundException.class).when(menuValidator).validateExistsMenuById(anyLong());

                    // then
                    verify(orderTableValidator).validateExistsOrderTableByIdAndEmptyIsFalse(anyLong());
                    assertThatThrownBy(() -> orderService.create(orderRequest))
                            .isInstanceOf(MenuNotFoundException.class);
                }),
                dynamicTest("비어있는 주문 테이블이 없을 경우 오류 발생됨.", () -> {
                    // given
                    OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 1L);
                    OrderRequest orderRequest = new OrderRequest(OrderStatus.COOKING, 1L, Arrays.asList(orderLineItemRequest));
                    doThrow(NonEmptyOrderTableNotFoundException.class).when(orderTableValidator).validateExistsOrderTableByIdAndEmptyIsFalse(anyLong());

                    // then
                    assertThatThrownBy(() -> orderService.create(orderRequest))
                            .isInstanceOf(NonEmptyOrderTableNotFoundException.class);
                })
        );
    }
}
