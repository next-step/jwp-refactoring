package kitchenpos.application;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menuGroup.MenuGroup;
import kitchenpos.domain.menuProduct.MenuProducts;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.orderLineItem.OrderLineItem;
import kitchenpos.domain.orderLineItem.OrderLineItemRepository;
import kitchenpos.domain.orderTable.OrderTable;
import kitchenpos.domain.orderTable.OrderTableRepository;
import kitchenpos.dto.order.OrderChangeStatusRequest;
import kitchenpos.dto.order.OrderRequest;
import kitchenpos.dto.order.OrderResponse;
import kitchenpos.dto.orderLineItem.OrderLineItemRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static kitchenpos.application.TableServiceTest.주문_테이블_데이터_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@DisplayName("주문 관련 테스트")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    MenuRepository menuRepository;

    @Mock
    OrderRepository orderRepository;

    @Mock
    OrderLineItemRepository orderLineItemRepository;

    @Mock
    OrderTableRepository orderTableRepository;

    @InjectMocks
    OrderService orderService;

    private OrderTable orderTable1;
    private OrderTable orderTable2;
    private Menu menu;

    @BeforeEach
    void setUp() {
        orderTable1 = 주문_테이블_데이터_생성(1L, null, 2, false);
        orderTable2 = 주문_테이블_데이터_생성(2L, null, 2, false);
        menu = new Menu(1L, "메뉴", BigDecimal.valueOf(20000), new MenuGroup(), new MenuProducts());
    }

    @DisplayName("주문을 생성할 수 있다")
    @Test
    void create() {
        // given
        List<OrderLineItem> orderLineItems = 주문_항목_목록_데이터_생성();
        OrderRequest request = 주문_요청_데이터_생성(1L, OrderStatus.COOKING, orderLineItems);
        Order 예상값 = 주문_데이터_생성(1L, orderTable1, OrderStatus.COOKING, orderLineItems);
        given(orderTableRepository.findByIdAndEmptyIsFalse(1L)).willReturn(Optional.of(orderTable1));
        given(menuRepository.findById(any())).willReturn(Optional.ofNullable(menu));
        given(menuRepository.findById(any())).willReturn(Optional.ofNullable(menu));
        given(menuRepository.countByIdIn(anyList())).willReturn(2L);
        given(orderRepository.save(any(Order.class))).willReturn(예상값);

        // when
        OrderResponse 주문_생성_결과 = 주문_생성(request);

        // then
        주문_데이터_비교(주문_생성_결과, OrderResponse.of(예상값));
    }

    @DisplayName("주문을 생성할 수 있다 - 주문 항목 1개 이상 있어야 한다")
    @Test
    void create_exception1() {
        // given
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        OrderRequest request = 주문_요청_데이터_생성(1L, OrderStatus.COOKING, orderLineItems);

        // when && then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("주문을 생성할 수 있다 - 유효한 메뉴이어야 한다")
    @Test
    void create_exception2() {
        // given
        List<OrderLineItem> orderLineItems = 주문_항목_목록_데이터_생성();
        OrderRequest request = 주문_요청_데이터_생성(1L, OrderStatus.COOKING, orderLineItems);
        given(orderTableRepository.findByIdAndEmptyIsFalse(1L)).willReturn(Optional.of(orderTable1));
        given(menuRepository.findById(any())).willReturn(Optional.ofNullable(menu));
        given(menuRepository.findById(any())).willReturn(Optional.ofNullable(menu));
        given(menuRepository.countByIdIn(anyList())).willReturn(1L);

        // when && then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 생성할 수 있다 - 주문 테이블이 빈 테이블이 아니어야 한다")
    @Test
    void create_exception3() {
        // given
        List<OrderLineItem> orderLineItems = 주문_항목_목록_데이터_생성();
        OrderRequest request = 주문_요청_데이터_생성(1L, OrderStatus.COOKING, orderLineItems);

        // when && then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("주문을 생성할 수 있다 - 중복된 메뉴가 있어서는 안된다")
    @Test
    void create_exception4() {
        // given
        List<OrderLineItem> orderLineItems = new ArrayList<>(주문_항목_목록_데이터_생성());
        orderLineItems.add(주문_항목_데이터_생성(2L, null, menu, 3));
        OrderRequest request = 주문_요청_데이터_생성(1L, OrderStatus.COOKING, orderLineItems);

        // when && then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("주문 목록을 조회할 수 있다")
    @Test
    void list() {
        // given
        List<Order> 예상값 = Arrays.asList(
                주문_데이터_생성(1L, orderTable1, OrderStatus.COOKING, 주문_항목_목록_데이터_생성()),
                주문_데이터_생성(2L, orderTable2, OrderStatus.COOKING, 주문_항목_목록_데이터_생성())
        );
        given(orderRepository.findAll()).willReturn(예상값);

        // when
        List<OrderResponse> 주문_목록_조회_결과 = 주문_목록_조회();

        // then
        assertAll(
                () -> 주문_데이터_비교(주문_목록_조회_결과.get(0), OrderResponse.of(예상값.get(0))),
                () -> 주문_데이터_비교(주문_목록_조회_결과.get(1), OrderResponse.of(예상값.get(1)))
        );
    }

    @DisplayName("주문 상태를 변경할 수 있다")
    @Test
    void changeOrderStatus() {
        // given
        Order 변경전_주문 = 주문_데이터_생성(1L, orderTable1, OrderStatus.MEAL, 주문_항목_목록_데이터_생성());
        OrderChangeStatusRequest 변경후_주문 = 주문_상태_수정_데이터_생성(1L, OrderStatus.MEAL, 주문_항목_목록_데이터_생성());
        given(orderTableRepository.findById(1L)).willReturn(Optional.ofNullable(orderTable1));
        given(menuRepository.findById(any())).willReturn(Optional.ofNullable(menu));
        given(menuRepository.findById(any())).willReturn(Optional.ofNullable(menu));
        given(orderRepository.findById(1L)).willReturn(Optional.of(변경전_주문));

        // when
        OrderResponse 주문_상태_변경_결과 = 주문_상태_변경(1L, 변경후_주문);

        // then
        assertThat(주문_상태_변경_결과.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @DisplayName("주문 상태를 변경할 수 있다 - 주문 상태가 '계산 완료' 상태가 아니어야 한다")
    @Test
    void changeOrderStatus_exception1() {
        // given
        Order 변경전_주문 = 주문_데이터_생성(1L, orderTable1, OrderStatus.COMPLETION, 주문_항목_목록_데이터_생성());
        OrderChangeStatusRequest 변경후_주문 = 주문_상태_수정_데이터_생성(1L, OrderStatus.MEAL, 주문_항목_목록_데이터_생성());
        given(orderTableRepository.findById(1L)).willReturn(Optional.ofNullable(orderTable1));
        given(menuRepository.findById(any())).willReturn(Optional.ofNullable(menu));
        given(menuRepository.findById(any())).willReturn(Optional.ofNullable(menu));
        given(orderRepository.findById(1L)).willReturn(Optional.of(변경전_주문));

        // when && then
        assertThatThrownBy(() -> 주문_상태_변경(1L, 변경후_주문)).isInstanceOf(IllegalArgumentException.class);
    }

    private OrderLineItem 주문_항목_데이터_생성(Long seq, Order order, Menu menu, long quantity) {
        return new OrderLineItem(seq, order, menu, quantity);
    }

    private OrderRequest 주문_요청_데이터_생성(Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        return new OrderRequest(orderTableId, orderStatus,
                orderLineItems.stream()
                        .map(OrderLineItemRequest::of)
                        .collect(Collectors.toList()));
    }

    private OrderChangeStatusRequest 주문_상태_수정_데이터_생성(Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        return new OrderChangeStatusRequest(orderTableId, orderStatus,
                orderLineItems.stream()
                        .map(OrderLineItemRequest::of)
                        .collect(Collectors.toList()));
    }

    private Order 주문_데이터_생성(Long id, OrderTable orderTable, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        return new Order(id, orderTable, orderStatus, orderLineItems);
    }

    private List<OrderLineItem> 주문_항목_목록_데이터_생성() {
        OrderLineItem orderLineItemId1 = 주문_항목_데이터_생성(1L, null, menu, 3);
        OrderLineItem orderLineItemId2 = 주문_항목_데이터_생성(2L, null, menu, 3);
        return Arrays.asList(orderLineItemId1, orderLineItemId2);
    }

    private OrderResponse 주문_생성(OrderRequest orderRequest) {
        return orderService.create(orderRequest);
    }

    private List<OrderResponse> 주문_목록_조회() {
        return orderService.list();
    }

    private OrderResponse 주문_상태_변경(Long orderId, OrderChangeStatusRequest orderChangeStatusRequest) {
        return orderService.changeOrderStatus(orderId, orderChangeStatusRequest);
    }

    private void 주문_데이터_비교(OrderResponse 주문_생성_결과, OrderResponse 예상값) {
        assertAll(
                () -> assertThat(주문_생성_결과.getId()).isEqualTo(예상값.getId()),
                () -> assertThat(주문_생성_결과.getOrderTableId()).isEqualTo(예상값.getOrderTableId()),
                () -> assertThat(주문_생성_결과.getOrderStatus()).isEqualTo(예상값.getOrderStatus()),
                () -> assertThat(주문_생성_결과.getOrderLineItems()).isEqualTo(예상값.getOrderLineItems())
        );
    }
}
