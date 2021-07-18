package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.exception.NotFoundMenuException;
import kitchenpos.order.OrderStatus;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderLineItemResponse;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.exception.EmptyOrderLineItemsException;
import kitchenpos.order.exception.NotEqualsOrderCountAndMenuCount;
import kitchenpos.order.exception.NotFoundOrderException;
import kitchenpos.order.mapper.OrderMapper;
import kitchenpos.order.validator.OrderValidator;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@DisplayName("주문 기능 관련 테스트")
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    private OrderLineItemRequest firstOrderLineItemRequest;
    private OrderLineItemRequest secondOrderLineItemRequest;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private OrderRepository orderRepository;

    private OrderValidator orderValidator;

    private OrderService orderService;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @BeforeEach
    public void setUp() {
        orderValidator = new OrderValidator(menuRepository, orderTableRepository);
        orderService = new OrderService(orderMapper, orderValidator, orderRepository);
    }

    @Test
    @DisplayName("주문항목이 없으면 예외발생")
    void isBlankOrderLineItem_exception() {
        // given
        // 주문 생성되어 있음
        OrderRequest orderRequest = new OrderRequest(1L, new ArrayList<>());
        OrderLineItems orderLineItems = new OrderLineItems(new ArrayList<OrderLineItem>());
        when(orderMapper.mapFormToOrder(orderRequest)).thenReturn(new Order(1L, 1L, orderLineItems));

        // when
        // 주문 테이블 등록되어 있음
        OrderTable orderTable = new OrderTable(10);
        when(orderTableRepository.findById(1L)).thenReturn(Optional.of(orderTable));

        // then
        // 주문 요청 시 오류 발생
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(EmptyOrderLineItemsException.class);
    }

    @Test
    @DisplayName("주문의 메뉴 수가 실제 메뉴 수와 맞지 않으면 예외발생")
    void isNotCollectQuantityMenuCount_exception() {
        // given
        // 주문 아이템 생성됨
        Menu firstMenu = new Menu(1L);
        OrderTable orderTable = new OrderTable(1L, 10);
        OrderLineItem firstOrderLineItem = new OrderLineItem(1L, 1L);
        OrderLineItem secondOrderLineItem = new OrderLineItem(2L, 1L);
        Order order = new Order(1L, new OrderLineItems(Arrays.asList(firstOrderLineItem, secondOrderLineItem)));

        // and
        // 주문 생성되어 있음
        OrderRequest orderRequest = new OrderRequest(1L, Arrays.asList(firstOrderLineItemRequest, secondOrderLineItemRequest));
        when(orderMapper.mapFormToOrder(orderRequest)).thenReturn(order);

        // when
        // 실제로 한개의 메뉴만 있음
        when(menuRepository.findByIdIn(Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(firstMenu));

        // and
        // 주문 테이블 등록되어 있음
        when(orderTableRepository.findById(1L)).thenReturn(Optional.of(orderTable));

        // then
        // 주문 생성 요청하면 오류 발생
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(NotEqualsOrderCountAndMenuCount.class);
    }

    @Test
    @DisplayName("주문 메뉴가 없는 예외처리")
    void isNotExistMenu() {
        // given
        // 주문 아이템 생성됨
        OrderLineItem firstOrderLineItem = new OrderLineItem(1L, 1L);
        OrderLineItem secondOrderLineItem = new OrderLineItem(2L, 1L);
        Order order = new Order(1L, new OrderLineItems(Arrays.asList(firstOrderLineItem, secondOrderLineItem)));

        // and
        // 주문 생성되어 있음
        OrderRequest orderRequest = new OrderRequest(1L, Arrays.asList(firstOrderLineItemRequest, secondOrderLineItemRequest));
        when(orderMapper.mapFormToOrder(orderRequest)).thenReturn(order);

        // and
        // 주문 테이블 등록되어 있음
        OrderTable orderTable = new OrderTable(1L, 10);
        when(orderTableRepository.findById(1L)).thenReturn(Optional.of(orderTable));

        // when
        // 메뉴가 존재하지 않음
        when(menuRepository.findByIdIn(Arrays.asList(1L, 2L))).thenReturn(new ArrayList<Menu>());


        // then
        // 주문 요청함
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(NotFoundMenuException.class);
    }

    @Test
    @DisplayName("주문 정상 접수 됨")
    void 주문_정상_접수() {
        // given
        // 주문 아이템 생성됨
        Menu firstMenu = new Menu(1L);
        Menu secondMenu = new Menu(2L);
        OrderTable orderTable = new OrderTable(1L, 10);
        OrderLineItem firstOrderLineItem = new OrderLineItem(1L, 1L);
        OrderLineItem secondOrderLineItem = new OrderLineItem(2L, 1L);
        Order order = new Order(1L, new OrderLineItems(Arrays.asList(firstOrderLineItem, secondOrderLineItem)));

        // and
        // 주문 생성되어 있음
        OrderRequest orderRequest = new OrderRequest(1L, Arrays.asList(firstOrderLineItemRequest, secondOrderLineItemRequest));
        when(orderMapper.mapFormToOrder(orderRequest)).thenReturn(order);

        // and
        // 두개의 메뉴 요청됨
        when(menuRepository.findByIdIn(Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(firstMenu, secondMenu));

        // and
        // 주문 테이블 등록되어 있음
        when(orderTableRepository.findById(1L)).thenReturn(Optional.of(orderTable));

        // when
        // 주문 요청함
        when(orderRepository.save(order)).thenReturn(order);
        OrderResponse expected = orderService.create(orderRequest);

        // than
        // 주문 접수됨
        assertThat(expected.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @Test
    @DisplayName("주문 조회 테스트")
    void 주문_조회_테스트() {
        // given
        // 주문 아이템 생성됨
        OrderTable orderTable = new OrderTable(1L, 10);
        OrderLineItem firstOrderLineItem = new OrderLineItem(1L, 1L);
        OrderLineItem secondOrderLineItem = new OrderLineItem(2L, 1L);
        Order order = new Order(1L, 1L, new OrderLineItems(Arrays.asList(firstOrderLineItem, secondOrderLineItem)));
        order.progressCook();

        // when
        // 주문 조회 함
        when(orderRepository.findAll()).thenReturn(Arrays.asList(order));

        // than
        // 주문 조회 됨
        List<OrderResponse> orderResponseList = orderService.list();
        assertThat(orderResponseList.get(0).getOrderLineItems()).containsAll(Arrays.asList(OrderLineItemResponse.of(firstOrderLineItem)
                , OrderLineItemResponse.of(secondOrderLineItem)));
    }

    @Test
    @DisplayName("주문 상태 변경 시 없는 주문 예외")
    void 주문_상태_변경_테스트() {
        // given
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        // then
        // 주문이 없음
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L))
                .isInstanceOf(NotFoundOrderException.class);
    }

    @Test
    @DisplayName("주문 상태 변경")
    void 주문_상태_변경() {
        // given
        // 주문 아이템 생성됨
        firstOrderLineItemRequest = 주문_아이템_생성(1L, 1L);
        secondOrderLineItemRequest = 주문_아이템_생성(2L, 1L);

        // and
        // 주문 메뉴 및 테이블 생성되어 있음
        Menu firstMenu = new Menu(1L);
        Menu secondMenu = new Menu(2L);
        OrderTable orderTable = new OrderTable(1L, 10);
        OrderLineItem firstOrderLineItem = new OrderLineItem(1L, 1L);
        OrderLineItem secondOrderLineItem = new OrderLineItem(1L, 1L);
        Order order = new Order(1L, new OrderLineItems(Arrays.asList(firstOrderLineItem, secondOrderLineItem)));
        order.changeOrderStatusComplete();

        // when
        // 주문 조회됨
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        // then
        // 상태 변경됨
        OrderResponse orderResponse = orderService.changeOrderStatus(1L);
        assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    private OrderLineItemRequest 주문_아이템_생성(Long menuId, Long quantity) {
        return new OrderLineItemRequest(menuId, quantity);
    }
}
