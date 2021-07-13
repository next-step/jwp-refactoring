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

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.exception.MenuNotFoundException;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.exception.OrderAlreadyExistsException;
import kitchenpos.order.exception.OrderNotFoundException;
import kitchenpos.product.domain.Product;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.exception.NonEmptyOrderTableNotFoundException;

@DisplayName("주문 서비스")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private TableService tableService;
    @Mock
    private MenuService menuService;
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private Menu menu;

    @BeforeEach
    void setUp() {
        MenuGroup menuGroup = new MenuGroup("AB");
        menu = new Menu("A", BigDecimal.valueOf(20000.00), menuGroup);
        menu.addMenuProduct(new MenuProduct(menu, new Product("a", BigDecimal.valueOf(15000.00)), 1));
        menu.addMenuProduct(new MenuProduct(menu, new Product("a", BigDecimal.valueOf(15000.00)), 1));
    }

    @TestFactory
    @DisplayName("모든 주문을 조회하는 기능")
    List<DynamicTest> find_allOrders1() {
        // mocking
        Order order = new Order(LocalDateTime.now(), new OrderTable(3, false));
        order.addOrderLineItem(new OrderLineItem(order, menu, 3L));
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
        Order order = new Order(LocalDateTime.now(), new OrderTable(3, false));
        order.addOrderLineItem(new OrderLineItem(order, menu, 3L));
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
        Order order = new Order(LocalDateTime.now(), orderTable);
        order.addOrderLineItem(new OrderLineItem(order, menu, 3L));

        given(tableService.findOrderTableByIdAndEmptyIsFalse(anyLong())).willReturn(orderTable);
        given(menuService.findMenuById(anyLong())).willReturn(menu);
        given(orderRepository.save(any(Order.class))).willReturn(order);

        // when
        OrderResponse resultOrderResponse = orderService.create(orderRequest);

        // then
        return Arrays.asList(
                dynamicTest("주문 초기 상태 확인됨.", () -> assertThat(resultOrderResponse.getOrderStatus()).isEqualTo(OrderStatus.COOKING)),
                dynamicTest("주문 테이블 확인됨.", () -> assertThat(resultOrderResponse.getOrderTableResponse()).isNotNull()),
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
                    given(tableService.findOrderTableByIdAndEmptyIsFalse(1L)).willReturn(new OrderTable(3, false));

                    // then
                    assertThatThrownBy(() -> orderService.create(orderRequest))
                            .isInstanceOf(IllegalArgumentException.class)
                            .hasMessage("입력된 주문 항목이 없습니다.");
                }),
                dynamicTest("비어있는 주문 테이블이 없을 경우 오류 발생됨.", () -> {
                    // given
                    OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 1L);
                    OrderRequest orderRequest = new OrderRequest(OrderStatus.COOKING, 1L, Arrays.asList(orderLineItemRequest));
                    given(tableService.findOrderTableByIdAndEmptyIsFalse(1L)).willThrow(NonEmptyOrderTableNotFoundException.class);

                    // then
                    assertThatThrownBy(() -> orderService.create(orderRequest))
                            .isInstanceOf(NonEmptyOrderTableNotFoundException.class);
                }),
                dynamicTest("주문 내역 메뉴에 등록되지 않은 메뉴가 포함되어 있을 경우 오류 발생됨.", () -> {
                    // given
                    OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 1L);
                    OrderRequest orderRequest = new OrderRequest(OrderStatus.COOKING, 2L, Arrays.asList(orderLineItemRequest));
                    given(tableService.findOrderTableByIdAndEmptyIsFalse(2L)).willReturn(new OrderTable(3, false));
                    given(menuService.findMenuById(1L)).willThrow(MenuNotFoundException.class);

                    // then
                    assertThatThrownBy(() -> orderService.create(orderRequest))
                            .isInstanceOf(MenuNotFoundException.class);
                })
        );
    }

    @TestFactory
    @DisplayName("수정할 수 없는 주문 확인")
    List<DynamicTest> cant_modify_order() {
        return Arrays.asList(
                dynamicTest("주문 목록 중 수정불가 항목이 포함되었을 경우", () -> {
                    // given
                    given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(true);

                    // then
                    assertThatThrownBy(() -> orderService.validateExistsOrdersStatusIsCookingOrMeal(Arrays.asList(1L)))
                            .isInstanceOf(OrderAlreadyExistsException.class)
                            .hasMessage("주문 상태가 COOKING 또는 MEAL인 주문이 존재합니다.");
                }),
                dynamicTest("입력 주문이 수정불가 상태일 경우", () -> {
                    // given
                    given(orderRepository.existsByOrderTableIdAndOrderStatusIn(anyLong(), any())).willReturn(true);

                    // then
                    assertThatThrownBy(() -> orderService.validateExistsOrderStatusIsCookingANdMeal(1L))
                            .isInstanceOf(OrderAlreadyExistsException.class)
                            .hasMessage("주문 상태가 COOKING 또는 MEAL인 주문이 존재합니다. 입력 ID : 1");
                })
        );
    }
}
