package kitchenpos.order.service;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.util.TestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @InjectMocks
    OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private TableService tableService;

    @Mock
    private MenuService menuService;

    private OrderLineItem 주문항목_1;
    private OrderLineItem 주문항목_2;
    private OrderLineItem 주문항목_3;
    private OrderTable 주문테이블_1;
    private OrderTable 주문테이블_2;
    private Order 주문_1;
    private Menu 메뉴;

    @BeforeEach
    void setUp() {
        주문항목_1 = OrderLineItem.of(1L, "후라이드치킨", new BigDecimal(16000), 1L);
        주문항목_생성(주문항목_1, 주문_1);
        주문항목_2 = OrderLineItem.of(2L, "양념치킨", new BigDecimal(16000),1L);
        주문항목_생성(주문항목_2, 주문_1);
        주문테이블_1 = OrderTable.of(1L, 2, false);
        주문_테이블_생성(주문테이블_1, 1L);
        주문_1 = Order.of(주문테이블_1.getId(), Arrays.asList(주문항목_1, 주문항목_2));
        메뉴 = 후라이드_치킨_메뉴_생성(1L, Arrays.asList(후라이드_메뉴_상품_생성()));
        메뉴_생성(메뉴);
    }

    @DisplayName("주문 등록")
    @Test
    void createOrder() {
        // given
        when(tableService.findById(주문테이블_1.getId()))
                .thenReturn(주문테이블_1);
        when(menuService.findById(1L))
                .thenReturn(메뉴);
        when(orderRepository.save(any()))
                .thenReturn(주문_1);

        // when
        OrderRequest 주문_요청 = new OrderRequest(1L, 주문_1.getOrderStatus(), 주문_1.getOrderedTime(),
                Arrays.asList(new OrderLineItemRequest(1L, 1L)));
        OrderResponse result = orderService.create(주문_요청);

        // then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(주문_1.getId()),
                () -> assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COOKING),
                () -> assertNotNull(result.getOrderedTime()),
                () -> assertThat(result.getOrderLineItems()).hasSize(2)
        );
    }

    @DisplayName("주문 등록 시 주문 항목 리스트가 없는 경우 등록 불가")
    @Test
    void createOrderAndOrderLineItemEmpty() {
        // given
        OrderRequest 주문_요청 = new OrderRequest(주문_1.getId(), 주문_1.getOrderStatus(), 주문_1.getOrderedTime(), null);

        // then
        assertThatThrownBy(() -> {
            orderService.create(주문_요청);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 등록 시 테이블이 미등록인 경우 등록 불가")
    @Test
    void createOrderAndNotExistTable() {
        // given
        when(tableService.findById(3L))
                .thenThrow(new IllegalArgumentException());
        // then
        OrderRequest 주문_요청 = new OrderRequest(3L, 주문_1.getOrderStatus(), 주문_1.getOrderedTime(),
                Arrays.asList(new OrderLineItemRequest(1L, 1L), new OrderLineItemRequest(2L, 1L)));
        assertThatThrownBy(() -> {
            orderService.create(주문_요청);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 등록 시 메뉴가 미등록인 경우 등록 불가")
    @Test
    void createOrderAndNotRegisterMenu() {
        // given
        when(tableService.findById(주문테이블_1.getId()))
                .thenReturn(주문테이블_1);
        when(menuService.findById(1L))
                .thenThrow(new IllegalArgumentException());

        // then
        OrderRequest 주문_요청 = new OrderRequest(1L, 주문_1.getOrderStatus(), 주문_1.getOrderedTime(),
                Arrays.asList(new OrderLineItemRequest(1L, 1L), new OrderLineItemRequest(2L, 1L)));
        assertThatThrownBy(() -> {
            orderService.create(주문_요청);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 등록 시 빈 테이블인 경우 등록 불가")
    @Test
    void createOrderAndEmptyTable() {
        // given
        when(tableService.findById(주문테이블_1.getId()))
                .thenThrow(new IllegalArgumentException());

        // then
        OrderRequest 주문_요청 = new OrderRequest(1L, 주문_1.getOrderStatus(), 주문_1.getOrderedTime(),
                Arrays.asList(new OrderLineItemRequest(1L, 1L), new OrderLineItemRequest(2L, 1L)));
        assertThatThrownBy(() -> {
            orderService.create(주문_요청);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 전체 조회")
    @Test
    void findAllOrders() {
        // given
        when(orderRepository.findAll())
                .thenReturn(Arrays.asList(주문_1));

        // when
        List<OrderResponse> list = orderService.list();

        // then
        assertThat(list).hasSize(1);
    }

    @DisplayName("주문 상태 변경")
    @Test
    void changeOrderStatusTest() {
        // given
        주문_1.changeOrderStatus(OrderStatus.MEAL);
        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(주문_1));
        when(orderRepository.save(any()))
                .thenReturn(주문_1);

        // when
        OrderRequest 주문_요청 = new OrderRequest(1L, OrderStatus.MEAL, null, null);
        OrderResponse result = orderService.changeOrderStatus(1L, 주문_요청);

        // then
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @DisplayName("주문 상태 변경 시 주문 완료인 경우 변경 불가")
    @Test
    void changeOrderStatusAndIsOrderStatusCompletion() {
        // given
        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(주문_1));

        // then
        OrderRequest 주문_요청 = new OrderRequest(1L, OrderStatus.COMPLETION, null, null);
        assertThatThrownBy(() -> {
            orderService.changeOrderStatus(1L, 주문_요청);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
